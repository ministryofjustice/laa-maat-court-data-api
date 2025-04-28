package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.helper.ReflectionHelper;
import gov.uk.courtdata.repository.ReservationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationsService {
    private final ReservationsRepository reservationsRepository;

    @Transactional(readOnly = true)
    public ReservationsEntity retrieve(Integer id) {
        return reservationsRepository.findById(id)
                .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("No Reservations found with Id: %d", id)));
    }

    @Transactional
    public void create(ReservationsEntity reservationsEntity) {
        reservationsRepository.save(reservationsEntity);
    }

    @Transactional
    public void update(Integer id, ReservationsEntity reservationsEntity) {
        ReservationsEntity existingReservationEntity = reservationsRepository.findById(id)
            .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("No Reservation found with ID: %d", id)));

        ReflectionHelper.updateEntityFromObject(existingReservationEntity, reservationsEntity);
        reservationsRepository.save(existingReservationEntity);
    }

    @Transactional
    public void delete(Integer id) {
        reservationsRepository.deleteById(id);
    }
}
