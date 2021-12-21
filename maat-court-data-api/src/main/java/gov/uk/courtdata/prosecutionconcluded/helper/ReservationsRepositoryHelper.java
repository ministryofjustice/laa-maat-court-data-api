package gov.uk.courtdata.prosecutionconcluded.helper;

import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationsRepositoryHelper {

    private final ReservationsRepository reservationsRepository;

    public boolean isMaatRecordLocked(Integer maatId) {

        log.info("Checking Maat-id Record Locked status");
        Optional<ReservationsEntity> reservationsEntity = reservationsRepository.findById(maatId);
        if (reservationsEntity.isPresent()) {
            log.info("Maat Record {} is locked by {} ", reservationsEntity.get().getRecordId(), reservationsEntity.get().getUserName());
            return true;
        } else {
            log.info("Maat Record is not locked");
            return false;
        }
    }
}
