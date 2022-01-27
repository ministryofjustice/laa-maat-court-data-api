package gov.uk.courtdata.prosecutionconcluded.helper;

import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.repository.ReservationsRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationsRepositoryHelperTest {

    @InjectMocks
    private ReservationsRepositoryHelper reservationsRepositoryHelper;

    @Mock
    private ReservationsRepository reservationsRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testWhenMaatIsNotLocked_thenReturnTrue() {

        Optional<ReservationsEntity> reservationsEntity = Optional
                .of(ReservationsEntity.builder()
                .build());
        when(reservationsRepository.findById(anyInt())).thenReturn(reservationsEntity);

        boolean status = reservationsRepositoryHelper.isMaatRecordLocked(anyInt());

        verify(reservationsRepository).findById(anyInt());

        assertAll("Imprisoned",
                () -> assertNotNull(status),
                () -> assertEquals(true, status));
    }

    @Test
    public void testWhenMaatIsLocked_thenReturnFalse() {

        Optional<ReservationsEntity> reservationsEntity = Optional.empty();
        when(reservationsRepository.findById(anyInt())).thenReturn(reservationsEntity);

        boolean status = reservationsRepositoryHelper.isMaatRecordLocked(anyInt());

        verify(reservationsRepository).findById(anyInt());

        assertAll("Imprisoned",
                () -> assertNotNull(status),
                () -> assertEquals(false, status));
    }
}