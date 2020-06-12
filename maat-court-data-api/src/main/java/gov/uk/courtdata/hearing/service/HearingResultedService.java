package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.hearing.crowncourt.service.CrownCourtHearingService;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class HearingResultedService {

    private final HearingValidationProcessor hearingValidationProcessor;

    private final HearingResultedImpl hearingResultedImpl;

    private final CrownCourtHearingService crownCourtHearingService;


    /**
     * Process Work Queue Processing for both Crown & Mags Court.
     * Process Crown Court Outcomes for CC
     */
    public void execute(final HearingResulted hearingResulted) {

        hearingValidationProcessor.validate(hearingResulted);

        switch (hearingResulted.getJurisdictionType()) {
            case CROWN:
                crownCourtHearingService.execute(hearingResulted);
                break;
            case MAGISTRATES:
                hearingResultedImpl.execute(hearingResulted);
                break;
            default:

        }


    }

}


