package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private HearingsService hearingsService;


    @Test
    public void test_whenMaatIsLocked_thenPublishMessageToSQS() {

        when(hearingsService.retrieveHearingForCaseConclusion(any())).thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(true);

        prosecutionConcludedService.execute(getProsecutionConcluded());

        //then
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(hearingsService, atLeast(1)).retrieveHearingForCaseConclusion(any());
        verify(prosecutionConcludedDataService, atLeast(1)).execute( any());
        verify(reservationsRepositoryHelper, atLeast(1)).isMaatRecordLocked(anyInt());
        verify(prosecutionConcludedImpl, never()).execute(any());
        verify(calculateOutcomeHelper, never()).calculate(any());

        verify(caseConclusionDTOBuilder, never()).build(any(), any(), any());
        verify(offenceHelper, never()).getTrialOffences(any(),anyInt());
    }

    @Test
    public void test_whenMultipleProsecutionMessagesAndFlagIsFalseOrTrue_thenProcessingValidOnly() {

        when(hearingsService.retrieveHearingForCaseConclusion(any())).thenReturn(getWQHearingEntity());


        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(false);
        when(offenceHelper.getTrialOffences(any(),anyInt())).thenReturn(List.of(getOffenceSummary("123")));

        prosecutionConcludedService.execute(getProsecutionConcluded());

        //then
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(hearingsService, atLeast(1)).retrieveHearingForCaseConclusion(any());
        verify(reservationsRepositoryHelper, atLeast(1)).isMaatRecordLocked(anyInt());
        verify(prosecutionConcludedImpl, atLeast(1)).execute(any());
        verify(calculateOutcomeHelper, atLeast(1)).calculate(any());
        verify(caseConclusionDTOBuilder, atLeast(1)).build(any(),any(),any());
        verify(offenceHelper, atLeast(1)).getTrialOffences(any(),anyInt());
    }


    @Test
    public void test_whenOffenceSummaryListIsEmpty_thenProcess() {

        when(hearingsService.retrieveHearingForCaseConclusion(any())).thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(false);
        when(offenceHelper.getTrialOffences(any(),anyInt())).thenReturn(List.of(getOffenceSummary("123")));

        prosecutionConcludedService.execute(getProsecutionConcluded());

        //then
        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(reservationsRepositoryHelper).isMaatRecordLocked(anyInt());
        // verify(prosecutionConcludedImpl,never()).execute(any());
        verify(calculateOutcomeHelper).calculate(any());
    }


    @Test
    public void givenMessageIsReceived_whenProsecutionConcluded_thenProcessingCCOutcome() {

        //given
        ProsecutionConcluded prosecutionConcludedRequest = getProsecutionConcluded();

        //when
        when(hearingsService.retrieveHearingForCaseConclusion(any())).thenReturn(getWQHearingEntity());

        when(reservationsRepositoryHelper.isMaatRecordLocked(any())).thenReturn(false);
        when(offenceHelper.getTrialOffences(any(),anyInt())).thenReturn(List.of(getOffenceSummary("123")));

        prosecutionConcludedService.execute(prosecutionConcludedRequest);

        //then
        verify(hearingsService, atLeast(1)).retrieveHearingForCaseConclusion(any());
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
        when(hearingsService.retrieveHearingForCaseConclusion(any()))
                .thenReturn(WQHearingEntity.builder().wqJurisdictionType(JurisdictionType.MAGISTRATES.name()).build());

        prosecutionConcludedService.execute(prosecutionConcludedRequest);

        //then

        verify(hearingsService, atLeast(1)).retrieveHearingForCaseConclusion(any());
        verify(prosecutionConcludedValidator).validateRequestObject(any(ProsecutionConcluded.class));
        verify(reservationsRepositoryHelper, never()).isMaatRecordLocked(anyInt());

        verify(prosecutionConcludedImpl, never()).execute(any(ConcludedDTO.class));
    }

    @Test
    public void givenMessageIsReceived_whenHearingDataNotInMaat_thenTriggerHearingProcessingViaCda() {

        when(hearingsService.retrieveHearingForCaseConclusion(any())).thenReturn(null);

        prosecutionConcludedService.execute(getProsecutionConcluded());

        verify(prosecutionConcludedValidator).validateRequestObject(any());
        verify(hearingsService, atLeast(1)).retrieveHearingForCaseConclusion(any());
        verify(reservationsRepositoryHelper, never()).isMaatRecordLocked(anyInt());
        verify(prosecutionConcludedImpl, never()).execute(any());
        verify(calculateOutcomeHelper, never()).calculate(any());
        verify(caseConclusionDTOBuilder, never()).build(any(), any(), any());
        verify(offenceHelper, never()).getTrialOffences(any(),anyInt());
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

    private WQHearingEntity getWQHearingEntity() {

        return WQHearingEntity.builder()
                                .maatId(1212111)
                                .caseId(1234)
                                .caseUrn("CaseUR")
                                .resultCodes("2322,3433")
                                .ouCourtLocation("OU")
                                .hearingUUID("ce60cac9-ab22-468e-8af9-a3ba2ecece5b")
                                .wqJurisdictionType(JurisdictionType.CROWN.name())
                                .build();
    }
}