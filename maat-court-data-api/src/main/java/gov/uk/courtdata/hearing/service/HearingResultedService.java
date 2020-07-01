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

import java.util.Optional;

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

        switch (hearingResulted.getJurisdictionType()) {
            case CROWN:
                processCrownCourtNotification(hearingResulted);
                break;
                case MAGISTRATES:
                    hearingResultedImpl.execute(hearingResulted);
                    break;
                default:
        }
    }

    private void processCrownCourtNotification(HearingResulted hearingResulted) {

        log.info("Processing crown court notification");
        if (isMaatRecordLocked(hearingResulted))
            publishMessageToHearingQueue(hearingResulted);
        else
            crownCourtHearingService.execute(hearingResulted);
    }

    private void publishMessageToHearingQueue (HearingResulted hearingResulted) {

        log.info("Message retry attempt no. "  + hearingResulted.getMessageRetryCounter());

        if (hearingResulted.getMessageRetryCounter()<=5) {
            hearingResultedPublisher.publish(hearingResulted);
        } else {
            throw new MaatRecordLockedException("Unable to process CP hearing notification because Maat Record is locked.");
        }
    }

    private boolean isMaatRecordLocked(HearingResulted hearingResulted) {

        log.info("Checking Maat Record Locked status");
        Optional<ReservationsEntity> reservationsEntity = reservationsRepository.findById(hearingResulted.getMaatId());
        if (reservationsEntity.isPresent()) {
            log.info("Maat Record {} is locked by {} ", reservationsEntity.get().getRecordId(), reservationsEntity.get().getUserName());
            return true;
        } else {
            log.info("Maat Record is not locked");
            return false;
        }
    }
}
