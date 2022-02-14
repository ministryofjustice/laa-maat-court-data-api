package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.HearingNotAvailableException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.prosecutionconcluded.builder.CaseConclusionDTOBuilder;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CalculateOutcomeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.OffenceHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.prosecutionconcluded.impl.ProsecutionConcludedImpl;
import gov.uk.courtdata.prosecutionconcluded.model.OffenceSummary;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.validator.ProsecutionConcludedValidator;
import gov.uk.courtdata.publisher.AwsStandardSqsPublisher;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProsecutionConcludedService {

    private final CalculateOutcomeHelper calculateOutcomeHelper;

    private final WQHearingRepository wqHearingRepository;

    private final ProsecutionConcludedValidator prosecutionConcludedValidator;

    private final ProsecutionConcludedImpl prosecutionConcludedImpl;

    private final ReservationsRepositoryHelper reservationsRepositoryHelper;

    private final AwsStandardSqsPublisher awsStandardSqsPublisher;

    private final CaseConclusionDTOBuilder caseConclusionDTOBuilder;

    private final OffenceHelper offenceHelper;


    public void execute(final ProsecutionConcluded prosecutionConcluded) {

        prosecutionConcludedValidator.validateRequestObject(prosecutionConcluded);

        WQHearingEntity wqHearingEntity = getWqHearingEntity(prosecutionConcluded);

        if (prosecutionConcluded.isConcluded()
                && wqHearingEntity != null
                && JurisdictionType.CROWN.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType())) {

            if (reservationsRepositoryHelper.isMaatRecordLocked(prosecutionConcluded.getMaatId())) {
                awsStandardSqsPublisher.publishMessageToProsecutionSQS(prosecutionConcluded);
            } else {
                List<OffenceSummary> offenceSummaryList = prosecutionConcluded.getOffenceSummary();
                List<OffenceSummary> trialOffences = offenceHelper
                        .getTrialOffences(offenceSummaryList, wqHearingEntity.getCaseId());

                if (!trialOffences.isEmpty()) {
                    processOutcome(prosecutionConcluded, wqHearingEntity, trialOffences);
                }
            }
        }
    }

    private void processOutcome(ProsecutionConcluded prosecutionConcluded, WQHearingEntity wqHearingEntity, List<OffenceSummary> trialOffences) {

        prosecutionConcludedValidator.validateOuCode(wqHearingEntity.getOuCourtLocation());
        String calculatedOutcome = calculateOutcomeHelper.calculate(trialOffences);
        log.info("calculated outcome is {} for this maat-id {}", calculatedOutcome, prosecutionConcluded.getMaatId());

        ConcludedDTO concludedDTO = caseConclusionDTOBuilder.build(prosecutionConcluded, wqHearingEntity, calculatedOutcome);

        prosecutionConcludedImpl.execute(concludedDTO);
    }


    private WQHearingEntity getWqHearingEntity(ProsecutionConcluded prosecutionConcluded) {
        List<WQHearingEntity> wqHearingEntityList = wqHearingRepository
                .findByMaatIdAndHearingUUID(prosecutionConcluded.getMaatId(), prosecutionConcluded.getHearingIdWhereChangeOccurred().toString());
        if (wqHearingEntityList.isEmpty()) {
            awsStandardSqsPublisher.publishingSqsMessageForHearing(prosecutionConcluded);
            throw new HearingNotAvailableException("No Hearing Entity found, re-publishing to the queue");
        }
        return wqHearingEntityList.get(0);
    }
}
