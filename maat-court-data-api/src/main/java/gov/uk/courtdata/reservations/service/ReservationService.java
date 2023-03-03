package gov.uk.courtdata.reservations.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.authentication.models.UserSessionType;
import gov.uk.courtdata.authentication.repository.UserAdminStoredProcedureRepository;
import gov.uk.courtdata.dto.ReservationDTO;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.ReservationsRepository;
import gov.uk.courtdata.reservations.Impl.ReservationImpl;
import gov.uk.courtdata.reservations.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;


@Service
@RequiredArgsConstructor
@Slf4j
@XRayEnabled
public class ReservationService {
    private final ReservationMapper reservationMapper;
    private final ReservationImpl reservationImpl;

    public ReservationDTO getReservation(Integer maatId) {
        ReservationsEntity reservation = reservationImpl.getReservation(maatId).orElseThrow(
                () -> new RequestedObjectNotFoundException(String.format("No reservation found for MAAT ID: %s", maatId)));
        return reservationMapper.reservationEntityToReservationDTO(reservation);
    }
}
