package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.exception.MaatRecordLockedException;
import gov.uk.courtdata.hearing.crowncourt.service.CrownCourtHearingService;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.ReservationsRepository;
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

    private final ReservationsRepository reservationsRepository;


    /**
     * Process Work Queue Processing for both Crown & Mags Court.
     * Process Crown Court Outcomes for CC
     * Check MAAT record status, if locked then put the back to the hearing queue with a delay of 15 minutes
     */
    public void execute(final HearingResulted hearingResulted) {

        hearingValidationProcessor.validate(hearingResulted);

        if (isMaatRecordLocked(hearingResulted)) {

            publishMessageToHearingQueue(hearingResulted);

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

    /**
     * Publishing a hearing resulted payload to a hearing queue.
     * @param hearingResulted
     */
    private void publishMessageToHearingQueue (HearingResulted hearingResulted) {

        log.info("Publishing a message payload to Hearing SQS. messageRetryCounter no. "  + hearingResulted.getMessageRetryCounter());

        if (hearingResulted.getMessageRetryCounter()<=5) {
            hearingResultedPublisher.publish(hearingResulted);
        } else {
            throw new MaatRecordLockedException("Unable to process CP hearing notification because Maat Record is locked.");
        }
    }

    /**
     * Calling a database to check the Maat Record lock status
     * @param hearingResulted
     * @return boolean
     */
    private boolean isMaatRecordLocked(HearingResulted hearingResulted) {

        ReservationsEntity reservationsEntity = reservationsRepository.getOne(hearingResulted.getMaatId().toString());
        if (reservationsEntity!=null) {
            log.info("Maat Record is locked ");
            return true;
        } else {
            log.info("Maat Record is not locked");
            return false;
        }
    }

}


