package gov.uk.courtdata.helper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.repository.ReservationsRepository;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReservationsRepositoryHelperTest {

    @InjectMocks
    private ReservationsRepositoryHelper reservationsRepositoryHelper;

    @Mock
    private ReservationsRepository reservationsRepository;

    @Test
    public void testWhenMaatIsNotLocked_thenReturnTrue() {

        Optional<ReservationsEntity> reservationsEntity =
                Optional.of(ReservationsEntity.builder().build());
        when(reservationsRepository.findById(anyInt())).thenReturn(reservationsEntity);

        Boolean status = reservationsRepositoryHelper.isMaatRecordLocked(anyInt());

        verify(reservationsRepository).findById(anyInt());

        assertAll("Imprisoned", () -> assertNotNull(status), () -> assertEquals(true, status));
    }

    @Test
    public void testWhenMaatIsLocked_thenReturnFalse() {

        Optional<ReservationsEntity> reservationsEntity = Optional.empty();
        when(reservationsRepository.findById(anyInt())).thenReturn(reservationsEntity);

        Boolean status = reservationsRepositoryHelper.isMaatRecordLocked(anyInt());

        verify(reservationsRepository).findById(anyInt());

        assertAll("Imprisoned", () -> assertNotNull(status), () -> assertEquals(false, status));
    }
}
