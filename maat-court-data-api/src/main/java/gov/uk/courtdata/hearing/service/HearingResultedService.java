package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hearing.crowncourt.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class HearingResultedService {

    private final HearingValidationProcessor hearingValidationProcessor;

    private final HearingResultedImpl hearingResultedImpl;

    private final CrownCourtProcessingImpl crownCourtProcessingImpl;



    /**
     * @param hearingResulted
     */
    public void process(final HearingResulted hearingResulted) {


        switch (hearingResulted.getJurisdictionType()) {

            case CROWN:
                crownCourtProcessingImpl.execute(hearingResulted);
                break;
            case MAGISTRATES:
                hearingValidationProcessor.validate(hearingResulted);
                hearingResultedImpl.execute(hearingResulted);
                break;
            default:
                throw new MAATCourtDataException(format("Invalid Jurisdiction type %s",
                        hearingResulted.getJurisdictionType()));
        }

    }

}
