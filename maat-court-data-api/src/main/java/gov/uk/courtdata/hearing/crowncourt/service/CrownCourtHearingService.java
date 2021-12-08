package gov.uk.courtdata.hearing.crowncourt.service;

import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.processor.WQHearingProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrownCourtHearingService {


    private final HearingResultedImpl hearingResultedImpl;

    private final WQHearingProcessor wqHearingProcessor;


    public void execute(final HearingResulted hearingResulted) {

        hearingResultedImpl.execute(hearingResulted);


        log.info("Create WQ hearing ");
        wqHearingProcessor.process(hearingResulted);

    }


}
