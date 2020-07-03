package gov.uk.courtdata.hearing.crowncourt.service;

import gov.uk.courtdata.hearing.crowncourt.impl.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.crowncourt.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.model.hearing.CCOutComeData;
import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrownCourtHearingService {

    private final CrownCourtValidationProcessor crownCourtValidationProcessor;
    private final CrownCourtProcessingImpl crownCourtProcessingImpl;
    private final HearingResultedImpl hearingResultedImpl;


    public void execute(final HearingResulted hearingResulted) {

        CCOutComeData ccOutComeData = hearingResulted.getCcOutComeData();
        if (isCrownCourtOutCome(ccOutComeData)) {
            executeCrownCourtOutCome(hearingResulted);
        }
        hearingResultedImpl.execute(hearingResulted);

    }
    private void executeCrownCourtOutCome(HearingResulted hearingResulted) {

        crownCourtValidationProcessor.validate(hearingResulted);
        crownCourtProcessingImpl.execute(hearingResulted);
        log.info("Crown Court Outcome Processing has been Completed for MAAT ID: {}", hearingResulted.getMaatId());
    }

    private boolean isCrownCourtOutCome(CCOutComeData ccOutComeData) {
        return ccOutComeData != null
                && ccOutComeData.getCcooOutcome() != null
                && !ccOutComeData.getCcooOutcome().isEmpty();
    }
}
