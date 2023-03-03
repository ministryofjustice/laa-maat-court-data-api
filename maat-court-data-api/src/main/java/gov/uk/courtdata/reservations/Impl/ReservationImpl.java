package gov.uk.courtdata.reservations.Impl;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class ReservationImpl {
    private final ReservationsRepository reservationsRepository;

    public Optional<ReservationsEntity> getReservation(Integer maatId)  {
        return reservationsRepository.findByRecordId(maatId);
    }
}
