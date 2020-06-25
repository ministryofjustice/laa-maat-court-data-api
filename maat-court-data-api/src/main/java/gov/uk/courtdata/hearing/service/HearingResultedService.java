package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.exception.MaatRecordLockedException;
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

    private final HearingResultedPublisher hearingResultedPublisher;


    /**
     * Process Work Queue Processing for both Crown & Mags Court.
     * Process Crown Court Outcomes for CC
     */
    public void execute(final HearingResulted hearingResulted) {

        hearingValidationProcessor.validate(hearingResulted);

        if (isMaatRecordLocked(hearingResulted)) {

            rePostMessageToQueue(hearingResulted);

        } else {

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

    private void rePostMessageToQueue (HearingResulted hearingResulted) {

        int messageRetryCounter = hearingResulted.getMessageRetryCounter()!=null?hearingResulted.getMessageRetryCounter():0;
        log.info("Posting a message to Hearing SQS. Reposting counter time  "  + messageRetryCounter);

        if (messageRetryCounter<=5) {
            //hearingResultedPublisher.publish(hearingResulted);

        } else {
            throw new MaatRecordLockedException("Maat Record is locked.");
        }
    }

    private boolean isMaatRecordLocked (HearingResulted hearingResulted) {
        //database call to check the status of a Maat Record. (using a SP)
        log.info("Maat Record is locked ");


        return true;

    }

}


