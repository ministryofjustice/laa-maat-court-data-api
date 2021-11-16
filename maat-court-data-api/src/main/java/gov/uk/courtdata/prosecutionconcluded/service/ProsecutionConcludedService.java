package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtOutComesValidator;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.model.crowncourt.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.CalculateCrownCourtOutcome;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProsecutionConcludedService {

    private final CalculateCrownCourtOutcome calculateCrownCourtOutcome;

    private final CrownCourtOutComesValidator crownCourtOutComesValidator;

    private final CrownCourtValidationProcessor crownCourtValidationProcessor;
    private final CrownCourtProcessingImpl crownCourtProcessingImpl;

    public void execute(final ProsecutionConcluded prosecutionConcluded) {

        //check type of the case ? cc or meg (only for CC)
        //get the request type by hearing-UUID from the newly created table WQHearingEntity
        //if (hearing type should only be crown) then process otherwise return and do nothing.


        String calculatedOutcome = calculateCrownCourtOutcome.calculate(prosecutionConcluded);
        log.info("calculated outcome is {} for this maat-id {}", calculatedOutcome, prosecutionConcluded.getMaatId());

        //TODO: check and make it generic
        //crownCourtOutComesValidator.validate(hearingRes);


        crownCourtValidationProcessor.validate(hearingResulted);
        crownCourtProcessingImpl.execute(hearingResulted);


        log.info("Crown Court Outcome Processing has been Completed for MAAT ID: {}", hearingResulted.getMaatId());


        crownCourtStoredProcedureRepository.updateCrownCourtOutcome(maatId,
                ccOutComeData.getCcOutcome(),
                crownCourtProcessHelper.isBenchWarrantIssued(hearingResulted),
                ccOutComeData.getAppealType() != null ? ccOutComeData.getAppealType() : repOrderEntity.getAptyCode(),
                crownCourtProcessHelper.isImprisoned(hearingResulted, ccOutComeData.getCcOutcome()),
                hearingResulted.getCaseUrn(),
                crownCourtCode);


        processSentencingDate(ccOutComeData.getCaseEndDate(), maatId, repOrderEntity.getCatyCaseType());

    }




}
