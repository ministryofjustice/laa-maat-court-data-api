package gov.uk.courtdata.helper;

import static org.assertj.core.api.Assertions.assertThat;
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
class ReservationsRepositoryHelperTest {

    @InjectMocks
    private ReservationsRepositoryHelper reservationsRepositoryHelper;

    @Mock
    private ReservationsRepository reservationsRepository;

    @Test
    void testWhenMaatIsNotLocked_thenReturnTrue() {

        Optional<ReservationsEntity> reservationsEntity =
                Optional.of(ReservationsEntity.builder().build());
        when(reservationsRepository.findById(anyInt())).thenReturn(reservationsEntity);

        Boolean status = reservationsRepositoryHelper.isMaatRecordLocked(anyInt());

        verify(reservationsRepository).findById(anyInt());

        assertThat(status).isNotNull().isTrue();
    }

    @Test
    void testWhenMaatIsLocked_thenReturnFalse() {

        Optional<ReservationsEntity> reservationsEntity = Optional.empty();
        when(reservationsRepository.findById(anyInt())).thenReturn(reservationsEntity);

        Boolean status = reservationsRepositoryHelper.isMaatRecordLocked(anyInt());

        verify(reservationsRepository).findById(anyInt());

        assertThat(status).isNotNull().isFalse();
    }
}
