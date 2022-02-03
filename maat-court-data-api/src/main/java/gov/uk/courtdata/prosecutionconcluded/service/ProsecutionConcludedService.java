package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CalculateOutcomeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.OffenceHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.prosecutionconcluded.impl.ProsecutionConcludedImpl;
import gov.uk.courtdata.prosecutionconcluded.model.OffenceSummary;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;


import gov.uk.courtdata.prosecutionconcluded.validator.ProsecutionConcludedValidator;
import gov.uk.courtdata.prosecutionconcluded.builder.CaseConclusionDTOBuilder;
import gov.uk.courtdata.publisher.AwsStandardSqsPublisher;
import gov.uk.courtdata.repository.WQHearingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

                prosecutionConcludedValidator.validateOuCode(wqHearingEntity.getOuCourtLocation());
                List<OffenceSummary> offenceSummaryList = prosecutionConcluded.getOffenceSummary();
                List<OffenceSummary> trialOffences = offenceHelper
                        .getTrialOffences(offenceSummaryList, wqHearingEntity.getCaseId());

                String calculatedOutcome = calculateOutcomeHelper.calculate(trialOffences);
                log.info("calculated outcome is {} for this maat-id {}", calculatedOutcome, prosecutionConcluded.getMaatId());

                ConcludedDTO concludedDTO = caseConclusionDTOBuilder.build(prosecutionConcluded, wqHearingEntity, calculatedOutcome);

                prosecutionConcludedImpl.execute(concludedDTO);

            }
        }
    }


    private WQHearingEntity getWqHearingEntity(ProsecutionConcluded prosecutionConcluded) {
        Optional<WQHearingEntity> wqHearingEntity = wqHearingRepository
                .findByMaatIdAndHearingUUID(prosecutionConcluded.getMaatId(), prosecutionConcluded.getHearingIdWhereChangeOccurred().toString());
        return wqHearingEntity.orElseThrow(
                () -> new ValidationException("WQHearingEntity is not found"));
    }
}
