package gov.uk.courtdata.reporder.service;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.ReservationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Optional;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static gov.uk.courtdata.builder.TestModelDataBuilder.RESERVATION_ID;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ReservationsService.class)
public class ReservationsServiceTest {

    @MockitoBean
    ReservationsRepository reservationsRepository;

    private ReservationsService reservationsService;

    @BeforeEach
    void setUp() {
        reservationsService = new ReservationsService(reservationsRepository);
    }

    @Test
    void givenValidId_whenRetrieveIsCalled_thenReservationIsReturned() {
        ReservationsEntity reservationsEntity = TestModelDataBuilder.getReservationsEntity();
        Mockito.when(reservationsRepository.findById(RESERVATION_ID))
                .thenReturn(Optional.of(reservationsEntity));

        assertThat(reservationsService.retrieve(RESERVATION_ID)).isEqualTo(reservationsEntity);
    }

    @Test
    void givenNonExistentId_whenRetrieveIsCalled_thenExceptionIsThrown() {
        assertThatThrownBy(
                () -> reservationsService.retrieve(1234)
        ).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessage("No Reservations found with Id: " + 1234);
    }

    @Test
    void givenValidReservation_whenCreateIsCalled_thenReservationIsSaved() {
        ReservationsEntity reservationsEntity = TestModelDataBuilder.getReservationsEntity();
        reservationsService.create(reservationsEntity);

        Mockito.verify(reservationsRepository, Mockito.times(1)).save(reservationsEntity);
    }

    @Test
    void givenValidReservation_whenUpdateIsCalled_thenReservationIsUpdated() {
        ReservationsEntity reservationsEntity = TestModelDataBuilder.getReservationsEntity();
        when(reservationsRepository.findById(RESERVATION_ID)).thenReturn(Optional.ofNullable(reservationsEntity));
        reservationsService.update(RESERVATION_ID, reservationsEntity);

        Mockito.verify(reservationsRepository, Mockito.times(1)).findById(RESERVATION_ID);
        Mockito.verify(reservationsRepository, Mockito.times(1)).save(reservationsEntity);
    }

    @Test
    void givenValidId_whenDeleteIsCalled_thenReservationIsDeleted() {
        reservationsService.delete(RESERVATION_ID);
        Mockito.verify(reservationsRepository, Mockito.times(1)).deleteById(RESERVATION_ID);
    }
}
