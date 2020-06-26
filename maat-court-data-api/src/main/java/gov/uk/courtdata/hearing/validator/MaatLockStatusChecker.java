package gov.uk.courtdata.hearing.validator;

import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MaatLockStatusChecker {

    private final ReservationsRepository reservationsRepository;

    public boolean isLocked(HearingResulted hearingResulted) {

        ReservationsEntity reservationsEntity =
                reservationsRepository.getOne(hearingResulted.getMaatId().toString());


        return false;

    }


}
