package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.enums.FunctionType;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.MaatRecordLockedException;
import gov.uk.courtdata.hearing.crowncourt.service.CrownCourtHearingService;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.processor.CourtApplicationsPreProcessor;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.helper.RepOrderCPDataHelper;
import gov.uk.courtdata.model.Defendant;
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
    private CrownCourtHearingService crownCourtHearingService;

    @Mock
    private ReservationsRepository reservationsRepository;

    @Mock
    private HearingResultedPublisher hearingResultedPublisher;

    @Mock
    private CourtApplicationsPreProcessor courtApplicationsPreProcessor;

    @Mock
    private RepOrderCPDataHelper repOrderCPDataHelper;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenAMagCourtNotification_whenMaatNotLocked_thenMagsCourtProcessingInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .jurisdictionType(JurisdictionType.MAGISTRATES)
                .defendant(Defendant.builder().defendantId("1211").build())
                .build();

        //when
        doNothing().when(hearingResultedImpl).execute(hearingDetails);

        when(repOrderCPDataHelper.getMaatIdByDefendantId(anyString())).thenReturn(12);

        hearingResultedService.execute(hearingDetails);
        //then
        verify(hearingValidationProcessor).validate(hearingDetails);
        verify(hearingResultedImpl).execute(hearingDetails);
        verify(repOrderCPDataHelper).getMaatIdByDefendantId(any());

    }

    @Test
    public void givenACrownCourtNotification_whenMaatRecordIsNotLocked_thenCrownCourtProcessingInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .defendant(Defendant.builder().defendantId("1211").build())
                .jurisdictionType(JurisdictionType.CROWN).build();
        //Optional<ReservationsEntity> reservationsEntity = Optional.empty();
        //when
        when(repOrderCPDataHelper.getMaatIdByDefendantId(anyString())).thenReturn(12);
        //when(reservationsRepository.findById(34)).thenReturn(reservationsEntity);
        doNothing().when(crownCourtHearingService).execute(hearingDetails);


        hearingResultedService.execute(hearingDetails);

        //verify
        verify(hearingValidationProcessor).validate(hearingDetails);
        verify(crownCourtHearingService).execute(hearingDetails);
        verify(repOrderCPDataHelper).getMaatIdByDefendantId(any());


    }

    @Test
    public void givenACrownCourtNotification_whenMaatRecordIsLocked_thenThrowException() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .jurisdictionType(JurisdictionType.CROWN)
                .defendant(Defendant.builder().defendantId("1211").build())
                .messageRetryCounter(6)
                .build();
        Optional<ReservationsEntity> reservationsEntity = Optional.of(ReservationsEntity.builder().recordId(34).userName("username-test").build());
        //when
        when(reservationsRepository.findById(anyInt())).thenReturn(reservationsEntity);
        //throw
        thrown.expect(MaatRecordLockedException.class);
        thrown.expectMessage("Unable to process CP hearing notification because Maat Record is locked.");

        hearingResultedService.execute(hearingDetails);
    }

    @Test
    public void givenACrownCourtNotification_whenMaatRecordIsLocked_thenPublishMessageToQueue() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .jurisdictionType(JurisdictionType.CROWN)
                .defendant(Defendant.builder().defendantId("121").build())
                .messageRetryCounter(4)
                .build();
        Optional<ReservationsEntity> reservationsEntity = Optional.of(ReservationsEntity.builder().recordId(34).userName("username-test").build());
        //when
        when(reservationsRepository.findById(34)).thenReturn(reservationsEntity);
        when(repOrderCPDataHelper.getMaatIdByDefendantId(anyString())).thenReturn(34);
        doNothing().when(hearingResultedPublisher).publish(hearingDetails);

        hearingResultedService.execute(hearingDetails);

        verify(hearingValidationProcessor).validate(hearingDetails);
        verify(hearingResultedPublisher).publish(hearingDetails);
    }

    @Test
    public void givenApplicationNotification_whenApplicationType_thenApplicationPreProcessingInvoked() {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .defendant(Defendant.builder().defendantId("1211").build())
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
        verify(repOrderCPDataHelper).getMaatIdByDefendantId(any());
    }
}
