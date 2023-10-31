package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.enums.FunctionType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.processor.CourtApplicationsPreProcessor;
import gov.uk.courtdata.hearing.processor.WQHearingProcessor;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class HearingResultedService {

    private final HearingValidationProcessor hearingValidationProcessor;

    private final HearingResultedImpl hearingResultedImpl;

    private final CourtApplicationsPreProcessor courtApplicationsPreProcessor;

    private final WQHearingProcessor wqHearingProcessor;

    /**
     * Process Work Queue Processing for both Crown & Mags Court.
     * Process Crown Court Outcomes for CC
     * Check MAAT record status, if locked then put the back to the hearing queue with a delay of 15 minutes
     */
    @Transactional(rollbackFor = MAATCourtDataException.class)
    public void execute(final HearingResulted hearingResulted) {

        hearingValidationProcessor.validate(hearingResulted);
        wqHearingProcessor.process(hearingResulted);
        if (FunctionType.APPLICATION == hearingResulted.getFunctionType()) {
            courtApplicationsPreProcessor.process(hearingResulted);
        }

        hearingResultedImpl.execute(hearingResulted);


    }
}