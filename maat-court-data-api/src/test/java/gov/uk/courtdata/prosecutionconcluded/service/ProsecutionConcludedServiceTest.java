package gov.uk.courtdata.prosecutionconcluded.service;

import com.google.gson.Gson;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CalculateOutcomeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.prosecutionconcluded.impl.ProsecutionConcludedImpl;
import gov.uk.courtdata.prosecutionconcluded.listner.request.ProsecutionConcludedValidator;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.OffenceSummary;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.Plea;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.ProsecutionConcluded;
import gov.uk.courtdata.publisher.AwsStandardSqsPublisher;
import gov.uk.courtdata.repository.WQHearingRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeast;

@RunWith(MockitoJUnitRunner.class)
public class ProsecutionConcludedServiceTest {

    @InjectMocks
    private ProsecutionConcludedService prosecutionConcludedService;

    @Mock
    private CalculateOutcomeHelper calculateOutcomeHelper;

    @Mock
    private WQHearingRepository wqHearingRepository;

    @Mock
    private ProsecutionConcludedValidator prosecutionConcludedValidator;

    @Mock
    private ProsecutionConcludedImpl prosecutionConcludedImpl;

    @Mock
    private ReservationsRepositoryHelper reservationsRepositoryHelper;

    @Mock
    private Gson gson;

    @Mock
    AwsStandardSqsPublisher awsStandardSqsPublisher;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_whenMaatIsLocked_thenPublishMessageToSQS() {

        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(),any()))
                .thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(true);

        prosecutionConcludedService.execute(getProsecutionConcluded());

        //then
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(wqHearingRepository,atLeast(1)).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(reservationsRepositoryHelper,atLeast(1)).isMaatRecordLocked(anyInt());
        verify(prosecutionConcludedImpl, never()).execute(any());
        verify(calculateOutcomeHelper,never()).calculate(any());
    }

    @Test
    public void test_whenMultipleProsecutionMessagesAndFlagIsFalseOrTrue_thenProcessingValidOnly() {

        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(),any()))
                .thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(false);

        prosecutionConcludedService.execute(getProsecutionConcluded());

        //then
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(wqHearingRepository,atLeast(1)).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(reservationsRepositoryHelper,atLeast(1)).isMaatRecordLocked(anyInt());
        verify(prosecutionConcludedImpl, atLeast(1)).execute(any());
        verify(calculateOutcomeHelper, atLeast(1)).calculate(any());
    }





    @Test
    public void test_whenOffenceSummaryListIsEmpty_thenProcess() {

        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(),any()))
                .thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(false);

        prosecutionConcludedService.execute(getProsecutionConcluded());

        //then
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(wqHearingRepository,atLeast(1)).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(reservationsRepositoryHelper).isMaatRecordLocked(anyInt());
       // verify(prosecutionConcludedImpl,never()).execute(any());
        verify(calculateOutcomeHelper).calculate(any());
    }


    @Test
    public void givenMessageIsReceived_whenProsecutionConcluded_thenProcessingCCOutcome() {

        //given
        ProsecutionConcluded prosecutionConcludedRequest = getProsecutionConcluded();

        //when
        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(),any()))
                .thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(false);

        prosecutionConcludedService.execute(prosecutionConcludedRequest);

        //then
        verify(wqHearingRepository).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(reservationsRepositoryHelper).isMaatRecordLocked(anyInt());
        verify(prosecutionConcludedImpl).execute(any());
        verify(calculateOutcomeHelper).calculate(any());


    }

    @Test
    public void givenMessageIsReceived_whenCaseIsMeg_thenNotProcess() {

        //given
        ProsecutionConcluded prosecutionConcludedRequest = getProsecutionConcluded();

        //when
        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(),any()))
                .thenReturn(Optional
                        .ofNullable(
                                WQHearingEntity.builder()
                                        .wqJurisdictionType(JurisdictionType.MAGISTRATES.name())
                                        .build()));

        prosecutionConcludedService.execute(prosecutionConcludedRequest);

        //then

        verify(wqHearingRepository).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(prosecutionConcludedValidator).validateRequestObject(any(ProsecutionConcluded.class));
        verify(reservationsRepositoryHelper,never()).isMaatRecordLocked(anyInt());
        verify(calculateOutcomeHelper,never()).calculate(any(ProsecutionConcluded.class));
        verify(prosecutionConcludedImpl,never()).execute(any(ConcludedDTO.class));
    }




    private ProsecutionConcluded getProsecutionConcluded() {
        return ProsecutionConcluded.builder()
                .concluded(true)
                .maatId(1221)
                .offenceSummaryList(Arrays.asList(getOffenceSummary("OF121")))
                .prosecutionCaseId(UUID.fromString("ce60cac9-ab22-468e-8af9-a3ba2ecece5b"))
                .hearingIdWhereChangeOccurred(UUID.fromString("ce60cac9-ab22-468e-8af9-a3ba2ecece5b"))
                .build();
    }

    private OffenceSummary getOffenceSummary(String offenceCode) {

        return OffenceSummary.builder()
                .offenceCode(offenceCode)
                .proceedingConcluded(true)
                .plea(Plea.builder().value("GUILTY").build())
                .proceedingsConcludedChangedDate("2012-12-12")
                .build();
    }

    private Optional<WQHearingEntity> getWQHearingEntity(){

        return Optional
                .ofNullable(
                        WQHearingEntity.builder()
                                .maatId(1212111)
                                .caseUrn("CaseUR")
                                .resultCodes("2322,3433")
                                .ouCourtLocation("OU")
                                .hearingUUID("ce60cac9-ab22-468e-8af9-a3ba2ecece5b")
                                .wqJurisdictionType(JurisdictionType.CROWN.name())
                                .build()
                );
    }
}