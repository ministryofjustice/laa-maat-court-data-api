package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.MaatRecordLockedException;
import gov.uk.courtdata.hearing.crowncourt.service.CrownCourtHearingService;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.repository.ReservationsRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HearingResultedServiceTest {

    @InjectMocks
    private HearingResultedService hearingResultedService;

    @Mock
    private HearingValidationProcessor hearingValidationProcessor;

    @Mock
    private HearingResultedImpl hearingResultedImpl;
    @Mock
    private CrownCourtHearingService crownCourtHearingService;

    @Mock
    ReservationsRepository reservationsRepository;

    @Mock
    HearingResultedPublisher hearingResultedPublisher;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenACaseDetail_whenCrownCourtHearingServiceIsReceived_thenCCImplIsInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .jurisdictionType(JurisdictionType.CROWN)
                .maatId(34)
                .messageRetryCounter(10)
                .build();

        when(reservationsRepository.getOne(anyString())).thenReturn(null);

        //when
        hearingResultedService.execute(hearingDetails);
        //then
        verify(crownCourtHearingService, times(1)).execute(hearingDetails);

    }

    @Test
    public void givenACaseDetail_whenMaatRecordIsLocked_thenThrowException() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .jurisdictionType(JurisdictionType.CROWN)
                .maatId(34)
                .messageRetryCounter(6)
                .build();
        ReservationsEntity reservationsEntity = ReservationsEntity.builder().build();
        //when
        when(reservationsRepository.getOne(anyString())).thenReturn(reservationsEntity);
        //throw
        thrown.expect(MaatRecordLockedException.class);
        thrown.expectMessage("Unable to process CP hearing notification because Maat Record is locked.");

        hearingResultedService.execute(hearingDetails);
    }

    @Test
    public void givenACaseDetail_whenMaatRecordIsLocked_thenPublishMessage() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .jurisdictionType(JurisdictionType.CROWN)
                .maatId(34)
                .messageRetryCounter(5)
                .build();
        ReservationsEntity reservationsEntity = ReservationsEntity.builder().build();
        //when
        when(reservationsRepository.getOne(anyString())).thenReturn(reservationsEntity);
        //when
        doNothing().when(hearingResultedPublisher).publish(hearingDetails);

        hearingResultedService.execute(hearingDetails);
    }


    @Test
    public void givenACaseDetail_whenMAGGCourtHearingServiceIsReceived_thenMagsCourtProcessingInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder().maatId(34).jurisdictionType(JurisdictionType.MAGISTRATES).build();

        //when
        when(reservationsRepository.getOne(anyString())).thenReturn(null);
        doNothing().when(hearingResultedImpl).execute(hearingDetails);

        hearingResultedService.execute(hearingDetails);
        //then
        verify(hearingValidationProcessor, times(1)).validate(hearingDetails);
        verify(hearingResultedImpl, times(1)).execute(hearingDetails);

    }



}
