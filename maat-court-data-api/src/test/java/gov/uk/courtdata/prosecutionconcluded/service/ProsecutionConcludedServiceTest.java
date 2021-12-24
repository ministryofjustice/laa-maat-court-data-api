package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.prosecutionconcluded.dto.ConcludedDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.CalculateOutcomeHelper;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.prosecutionconcluded.impl.ProsecutionConcludedImpl;
import gov.uk.courtdata.prosecutionconcluded.listner.request.ProsecutionConcludedValidator;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.OffenceSummary;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.Plea;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.ProsecutionConcludedRequest;
import gov.uk.courtdata.repository.WQHearingRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenMessageIsReceived_whenProsecutionConcluded_thenProcessingCCOutcome() {

        //given
        ProsecutionConcludedRequest prosecutionConcludedRequest = getProsecutionConcludedRequest();

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
        ProsecutionConcludedRequest prosecutionConcludedRequest = getProsecutionConcludedRequest();

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
        verify(prosecutionConcludedValidator).validateRequestObject(any(ProsecutionConcludedRequest.class));
        verify(reservationsRepositoryHelper,never()).isMaatRecordLocked(anyInt());
        verify(calculateOutcomeHelper,never()).calculate(any(ProsecutionConcluded.class));
        verify(prosecutionConcludedImpl,never()).execute(any(ConcludedDTO.class));
    }



    @Test (expected = MAATCourtDataException.class)
    public void givenMessageIsReceived_whenProsecutionConcludedIsEmpty_thenThrowEx() {

       ProsecutionConcludedRequest request =
               ProsecutionConcludedRequest.builder()
                       .prosecutionConcludedList(Arrays.asList())
                       .build();
        prosecutionConcludedService.execute(request);
    }

        private ProsecutionConcludedRequest getProsecutionConcludedRequest () {
        return ProsecutionConcludedRequest.builder()
                .prosecutionConcludedList(Arrays.asList(prosecutionConcluded()))
                .build();
    }

    private ProsecutionConcluded prosecutionConcluded() {
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