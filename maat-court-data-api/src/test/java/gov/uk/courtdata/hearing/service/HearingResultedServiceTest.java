package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.enums.FunctionType;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.processor.CourtApplicationsPreProcessor;
import gov.uk.courtdata.hearing.processor.WQHearingProcessor;
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

import java.util.Optional;

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
    private ReservationsRepository reservationsRepository;

//    @Mock
//    private HearingResultedPublisher hearingResultedPublisher;

    @Mock
    private CourtApplicationsPreProcessor courtApplicationsPreProcessor;

    @Mock
    private WQHearingProcessor wqHearingProcessor;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenAMagCourtNotification_whenMaatNotLocked_thenMagsCourtProcessingInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder().maatId(34).jurisdictionType(JurisdictionType.MAGISTRATES).build();

        //when
        doNothing().when(hearingResultedImpl).execute(hearingDetails);

        hearingResultedService.execute(hearingDetails);
        //then
        verify(hearingValidationProcessor).validate(hearingDetails);
        verify(hearingResultedImpl).execute(hearingDetails);
    }

    @Test
    public void givenACrownCourtNotification_whenMaatRecordIsNotLocked_thenCrownCourtProcessingInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder().maatId(34).jurisdictionType(JurisdictionType.CROWN).build();
        Optional<ReservationsEntity> reservationsEntity = Optional.empty();
        //when
        //when(reservationsRepository.findById(34)).thenReturn(reservationsEntity);

        hearingResultedService.execute(hearingDetails);

        //verify
        verify(hearingValidationProcessor).validate(hearingDetails);
        verify(wqHearingProcessor).process(hearingDetails);

    }

//    @Test
//    public void givenACrownCourtNotification_whenMaatRecordIsLocked_thenThrowException() {
//
//        //given
//        HearingResulted hearingDetails = HearingResulted.builder()
//                .jurisdictionType(JurisdictionType.CROWN)
//                .maatId(34)
//                .messageRetryCounter(6)
//                .build();
//        Optional<ReservationsEntity> reservationsEntity = Optional.of(ReservationsEntity.builder().recordId(34).userName("username-test").build());
//        //when
//        when(reservationsRepository.findById(anyInt())).thenReturn(reservationsEntity);
//        //throw
//        thrown.expect(MaatRecordLockedException.class);
//        thrown.expectMessage("Unable to process CP hearing notification because Maat Record is locked.");
//
//        hearingResultedService.execute(hearingDetails);
//    }

//    @Test
//    public void givenACrownCourtNotification_whenMaatRecordIsLocked_thenPublishMessageToQueue() {
//
//        //given
//        HearingResulted hearingDetails = HearingResulted.builder()
//                .jurisdictionType(JurisdictionType.CROWN)
//                .maatId(34)
//                .messageRetryCounter(4)
//                .build();
//        Optional<ReservationsEntity> reservationsEntity = Optional.of(ReservationsEntity.builder().recordId(34).userName("username-test").build());
//        //when
//        when(reservationsRepository.findById(34)).thenReturn(reservationsEntity);
//        doNothing().when(hearingResultedPublisher).publish(hearingDetails);
//
//        hearingResultedService.execute(hearingDetails);
//
//        verify(hearingValidationProcessor).validate(hearingDetails);
//        verify(hearingResultedPublisher).publish(hearingDetails);
//    }

    @Test
    public void givenApplicationNotification_whenApplicationType_thenApplicationPreProcessingInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .maatId(34)
                .functionType(FunctionType.APPLICATION)
                .jurisdictionType(JurisdictionType.MAGISTRATES)
                .build();

        //when
        doNothing().when(courtApplicationsPreProcessor).process(hearingDetails);

        hearingResultedService.execute(hearingDetails);
        //then
        verify(courtApplicationsPreProcessor).process(hearingDetails);
        verify(hearingValidationProcessor).validate(hearingDetails);
        verify(hearingResultedImpl).execute(hearingDetails);
    }
}
