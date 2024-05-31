package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.prosecutionconcluded.builder.CaseConclusionDTOBuilder;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CalculateOutcomeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.OffenceHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.prosecutionconcluded.impl.ProsecutionConcludedImpl;
import gov.uk.courtdata.prosecutionconcluded.model.OffenceSummary;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.validator.ProsecutionConcludedValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProsecutionConcludedService {

    private final CalculateOutcomeHelper calculateOutcomeHelper;

    private final ProsecutionConcludedValidator prosecutionConcludedValidator;

    private final ProsecutionConcludedImpl prosecutionConcludedImpl;

    private final ReservationsRepositoryHelper reservationsRepositoryHelper;

    private final CaseConclusionDTOBuilder caseConclusionDTOBuilder;

    private final OffenceHelper offenceHelper;

    private final ProsecutionConcludedDataService prosecutionConcludedDataService;

    private final HearingsService hearingsService;


    public void execute(final ProsecutionConcluded prosecutionConcluded) {

        log.info("CC Outcome process is kicked off for  maat-id {}", prosecutionConcluded.getMaatId());
        LoggingData.MAAT_ID.putInMDC(prosecutionConcluded.getMaatId());
        prosecutionConcludedValidator.validateRequestObject(prosecutionConcluded);

        WQHearingEntity wqHearingEntity = hearingsService.retrieveHearingForCaseConclusion(prosecutionConcluded);

        if (prosecutionConcluded.isConcluded()
                && wqHearingEntity != null
                && JurisdictionType.CROWN.name().equalsIgnoreCase(wqHearingEntity.getWqJurisdictionType())) {

            if (reservationsRepositoryHelper.isMaatRecordLocked(prosecutionConcluded.getMaatId())) {
                prosecutionConcludedDataService.execute(prosecutionConcluded);
            } else {
                executeCCOutCome(prosecutionConcluded, wqHearingEntity);
            }
        }

    }

    public void executeCCOutCome(ProsecutionConcluded prosecutionConcluded, WQHearingEntity wqHearingEntity) {
        List<OffenceSummary> offenceSummaryList = prosecutionConcluded.getOffenceSummary();
        List<OffenceSummary> trialOffences = offenceHelper
                .getTrialOffences(offenceSummaryList, prosecutionConcluded.getMaatId());

        if (!trialOffences.isEmpty()) {
            log.info("Number of Valid offences for CC Outcome Calculations : {}", trialOffences.size());
            processOutcome(prosecutionConcluded, wqHearingEntity, trialOffences);
        }
        prosecutionConcludedDataService.updateConclusion(prosecutionConcluded.getMaatId());
        log.info("CC Outcome is completed for  maat-id {}", prosecutionConcluded.getMaatId());
    }

    private void processOutcome(ProsecutionConcluded prosecutionConcluded, WQHearingEntity wqHearingEntity, List<OffenceSummary> trialOffences) {

        prosecutionConcludedValidator.validateOuCode(wqHearingEntity.getOuCourtLocation());
        String calculatedOutcome = calculateOutcomeHelper.calculate(trialOffences);
        log.info("calculated outcome is {} for this maat-id {}", calculatedOutcome, prosecutionConcluded.getMaatId());

        ConcludedDTO concludedDTO = caseConclusionDTOBuilder.build(prosecutionConcluded, wqHearingEntity, calculatedOutcome);

        prosecutionConcludedImpl.execute(concludedDTO);
    }
}
