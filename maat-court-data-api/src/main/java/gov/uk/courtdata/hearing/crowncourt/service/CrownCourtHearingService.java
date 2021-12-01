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

        hearingResultedImpl.execute(hearingResulted);

        executeCrownCourtOutCome(hearingResulted);

    }

    private boolean isCaseConcluded(HearingResulted hearingResulted) {
        CCOutComeData ccOutComeData = hearingResulted.getCcOutComeData();
        return ccOutComeData != null && ccOutComeData.getCcooOutcome() != null
                && hearingResulted.getDefendant().getOffences()
                .stream()
                .allMatch(f -> f.getVerdict() != null
                        && f.getVerdict().getCategoryType() != null);
    }


    public void executeCrownCourtOutCome(HearingResulted hearingResulted) {

        if (isCaseConcluded(hearingResulted)) {
            crownCourtValidationProcessor.validate(hearingResulted);
            crownCourtProcessingImpl.execute(hearingResulted);
            log.info("Crown Court Outcome Processing has been Completed for MAAT ID: {}", hearingResulted.getMaatId());
        }
    }

}
