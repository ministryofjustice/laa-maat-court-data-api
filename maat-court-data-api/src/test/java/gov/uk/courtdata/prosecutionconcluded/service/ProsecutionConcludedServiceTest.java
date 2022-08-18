package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.courtdataadapter.client.CourtDataAdapterClient;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.Metadata;
import gov.uk.courtdata.prosecutionconcluded.builder.CaseConclusionDTOBuilder;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CalculateOutcomeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.OffenceHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.prosecutionconcluded.impl.ProsecutionConcludedImpl;
import gov.uk.courtdata.prosecutionconcluded.model.OffenceSummary;
import gov.uk.courtdata.prosecutionconcluded.model.Plea;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.validator.ProsecutionConcludedValidator;
import gov.uk.courtdata.repository.WQHearingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    private CaseConclusionDTOBuilder caseConclusionDTOBuilder;

    @Mock
    private OffenceHelper offenceHelper;

    @Mock
    private ProsecutionConcludedDataService prosecutionConcludedDataService;

    @Mock
    private CourtDataAdapterClient courtDataAdapterClient;


    @Test
    public void test_whenMaatIsLocked_thenPublishMessageToSQS() {

        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(), any()))
                .thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(true);

        prosecutionConcludedService.execute(getProsecutionConcluded());

        //then
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(wqHearingRepository, atLeast(1)).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(prosecutionConcludedDataService, atLeast(1)).execute( any());
        verify(reservationsRepositoryHelper, atLeast(1)).isMaatRecordLocked(anyInt());
        verify(prosecutionConcludedImpl, never()).execute(any());
        verify(calculateOutcomeHelper, never()).calculate(any());

        verify(caseConclusionDTOBuilder, never()).build(any(), any(), any());
        verify(offenceHelper, never()).getTrialOffences(any(),anyInt());
        verify(courtDataAdapterClient, never()).triggerHearingProcessing(any(), any());
    }

    @Test
    public void test_whenMultipleProsecutionMessagesAndFlagIsFalseOrTrue_thenProcessingValidOnly() {

        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(), any()))
                .thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(false);
        when(offenceHelper.getTrialOffences(any(),anyInt())).thenReturn(List.of(getOffenceSummary("123")));

        prosecutionConcludedService.execute(getProsecutionConcluded());

        //then
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(wqHearingRepository, atLeast(1)).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(reservationsRepositoryHelper, atLeast(1)).isMaatRecordLocked(anyInt());
        verify(prosecutionConcludedImpl, atLeast(1)).execute(any());
        verify(calculateOutcomeHelper, atLeast(1)).calculate(any());
        verify(caseConclusionDTOBuilder, atLeast(1)).build(any(),any(),any());
        verify(offenceHelper, atLeast(1)).getTrialOffences(any(),anyInt());
        verify(courtDataAdapterClient, never()).triggerHearingProcessing(any(), any());
    }


    @Test
    public void test_whenOffenceSummaryListIsEmpty_thenProcess() {

        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(), any()))
                .thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(false);
        when(offenceHelper.getTrialOffences(any(),anyInt())).thenReturn(List.of(getOffenceSummary("123")));

        prosecutionConcludedService.execute(getProsecutionConcluded());

        //then
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(wqHearingRepository, atLeast(1)).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(reservationsRepositoryHelper).isMaatRecordLocked(anyInt());
        // verify(prosecutionConcludedImpl,never()).execute(any());
        verify(calculateOutcomeHelper).calculate(any());
        verify(courtDataAdapterClient, never()).triggerHearingProcessing(any(), any());
    }


    @Test
    public void givenMessageIsReceived_whenProsecutionConcluded_thenProcessingCCOutcome() {

        //given
        ProsecutionConcluded prosecutionConcludedRequest = getProsecutionConcluded();

        //when
        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(), any()))
                .thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(false);
        when(offenceHelper.getTrialOffences(any(),anyInt())).thenReturn(List.of(getOffenceSummary("123")));

        prosecutionConcludedService.execute(prosecutionConcludedRequest);

        //then
        verify(wqHearingRepository).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(reservationsRepositoryHelper).isMaatRecordLocked(anyInt());
        verify(prosecutionConcludedImpl).execute(any());
        verify(calculateOutcomeHelper).calculate(any());
        verify(courtDataAdapterClient, never()).triggerHearingProcessing(any(), any());

    }

    @Test
    public void givenMessageIsReceived_whenCaseIsMeg_thenNotProcess() {

        //given
        ProsecutionConcluded prosecutionConcludedRequest = getProsecutionConcluded();

        //when
        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(), any()))
                .thenReturn(List.of(
                        WQHearingEntity.builder()
                                .wqJurisdictionType(JurisdictionType.MAGISTRATES.name())
                                .build()));

        prosecutionConcludedService.execute(prosecutionConcludedRequest);

        //then

        verify(wqHearingRepository).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(prosecutionConcludedValidator).validateRequestObject(any(ProsecutionConcluded.class));
        verify(reservationsRepositoryHelper, never()).isMaatRecordLocked(anyInt());

        verify(prosecutionConcludedImpl, never()).execute(any(ConcludedDTO.class));
        verify(courtDataAdapterClient, never()).triggerHearingProcessing(any(), any());
    }

    @Test
    public void givenMessageIsReceived_whenHearingDataNotInMaat_thenTriggerHearingProcessingViaCda() {

        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(), any()))
                .thenReturn(new ArrayList<>());

        prosecutionConcludedService.execute(getProsecutionConcluded());

        verifyCallsForHearingTriggerScenario();
    }

    @Test
    public void givenMessageIsReceived_whenHearingDataNotInMaatAndCdaCallErrors_thenTheErrorIsHandled() {

        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(), any()))
                .thenReturn(new ArrayList<>());

        doThrow(new MAATCourtDataException("Test CDA call Error")).when(courtDataAdapterClient)
                .triggerHearingProcessing(any(), any());

        prosecutionConcludedService.execute(getProsecutionConcluded());

        verifyCallsForHearingTriggerScenario();
    }

    private void verifyCallsForHearingTriggerScenario() {
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(wqHearingRepository, atLeast(1)).findByMaatIdAndHearingUUID(anyInt(), any());
        verify(prosecutionConcludedDataService, atLeast(1)).execute( any());
        verify(reservationsRepositoryHelper, never()).isMaatRecordLocked(anyInt());
        verify(prosecutionConcludedImpl, never()).execute(any());
        verify(calculateOutcomeHelper, never()).calculate(any());
        verify(caseConclusionDTOBuilder, never()).build(any(), any(), any());
        verify(offenceHelper, never()).getTrialOffences(any(),anyInt());
        verify(courtDataAdapterClient, atLeastOnce()).triggerHearingProcessing(any(), any());
    }


    private ProsecutionConcluded getProsecutionConcluded() {
        return ProsecutionConcluded.builder()
                .isConcluded(true)
                .maatId(1221)
                .offenceSummary(List.of(getOffenceSummary("OF121")))
                .prosecutionCaseId(UUID.fromString("ce60cac9-ab22-468e-8af9-a3ba2ecece5b"))
                .hearingIdWhereChangeOccurred(UUID.fromString("ce60cac9-ab22-468e-8af9-a3ba2ecece5b"))
                .metadata(Metadata.builder().laaTransactionId(UUID.randomUUID().toString()).build())
                .build();
    }

    private OffenceSummary getOffenceSummary(String offenceCode) {

        return OffenceSummary.builder()
                .offenceCode(offenceCode)
                .proceedingsConcluded(true)
                .plea(Plea.builder().value("GUILTY").build())
                .proceedingsConcludedChangedDate("2012-12-12")
                .build();
    }

    private List<WQHearingEntity> getWQHearingEntity() {

        return List
                .of(
                        WQHearingEntity.builder()
                                .maatId(1212111)
                                .caseId(1234)
                                .caseUrn("CaseUR")
                                .resultCodes("2322,3433")
                                .ouCourtLocation("OU")
                                .hearingUUID("ce60cac9-ab22-468e-8af9-a3ba2ecece5b")
                                .wqJurisdictionType(JurisdictionType.CROWN.name())
                                .build()
                );
    }
}