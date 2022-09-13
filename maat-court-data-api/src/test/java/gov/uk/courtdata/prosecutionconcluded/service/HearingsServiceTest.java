package gov.uk.courtdata.prosecutionconcluded.service;

import gov.uk.courtdata.courtdataadapter.client.CourtDataAdapterClient;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.Metadata;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.WQHearingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HearingsServiceTest {
    private final UUID hearingUuid = UUID.randomUUID();
    private final Integer maatId = 12345;
    private final String laaTransactionId = UUID.randomUUID().toString();

    @InjectMocks
    private HearingsService hearingsService;

    @Mock
    private CourtDataAdapterClient courtDataAdapterClient;

    @Mock
    private WQHearingRepository wqHearingRepository;

    @Mock
    private ProsecutionConcludedDataService prosecutionConcludedDataService;
    @Test
    void givenAHearingThatDoesNotExist_whenRetrieveHearingForCaseConclusionIsInvoked_thenHearingProcessingIsTriggered() {
        runMissingHearingScenario();
    }

    @Test
    void givenAHearingThatDoesNotExist_whenRetrieveHearingForCaseConclusionIsInvoked_thenErrorsDuringHearingProcessingAreHandled() {
        doThrow(new MAATCourtDataException("Test CDA call Error")).when(courtDataAdapterClient)
                .triggerHearingProcessing(any(), any());
        runMissingHearingScenario();
    }

    @Test
    void givenAValidHearingID_whenRetrieveHearingForCaseConclusionIsInvoked_thenTheHearingIsReturned() {
        ProsecutionConcluded prosecutionConcluded = getProsecutionConcluded();
        WQHearingEntity testHearing = WQHearingEntity.builder().hearingUUID(hearingUuid.toString()).build();
        when(wqHearingRepository.findByMaatIdAndHearingUUID(maatId, hearingUuid.toString()))
                .thenReturn(List.of(testHearing));

        assertEquals(hearingsService.retrieveHearingForCaseConclusion(prosecutionConcluded), testHearing);

        verify(wqHearingRepository, atLeast(1)).findByMaatIdAndHearingUUID(maatId, hearingUuid.toString());
        verify(courtDataAdapterClient, never()).triggerHearingProcessing(hearingUuid, laaTransactionId);
        verify(wqHearingRepository, atLeast(1)).findByMaatIdAndHearingUUID(maatId, hearingUuid.toString());

    }


    private void runMissingHearingScenario() {
        ProsecutionConcluded prosecutionConcluded = getProsecutionConcluded();

        when(wqHearingRepository.findByMaatIdAndHearingUUID(maatId, hearingUuid.toString())).thenReturn(new ArrayList<>());

        assertNull(hearingsService.retrieveHearingForCaseConclusion(prosecutionConcluded));

        verify(wqHearingRepository, atLeast(1)).findByMaatIdAndHearingUUID(maatId, hearingUuid.toString());
        verify(prosecutionConcludedDataService, atLeastOnce()).execute(prosecutionConcluded);

    }

    private ProsecutionConcluded getProsecutionConcluded() {
        return ProsecutionConcluded.builder()
                .hearingIdWhereChangeOccurred(hearingUuid)
                .isConcluded(true)
                .maatId(maatId)
                .metadata(Metadata.builder().laaTransactionId(laaTransactionId).build())
                .build();
    }
}