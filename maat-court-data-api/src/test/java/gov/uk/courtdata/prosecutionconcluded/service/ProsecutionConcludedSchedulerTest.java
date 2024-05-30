package gov.uk.courtdata.prosecutionconcluded.service;

import com.google.gson.Gson;
import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProsecutionConcludedSchedulerTest {

    @InjectMocks
    private ProsecutionConcludedScheduler prosecutionConcludedScheduler;
    @Mock
    private ProsecutionConcludedRepository prosecutionConcludedRepository;
    @Mock
    private ProsecutionConcludedService prosecutionConcludedService;
    @Mock
    private HearingsService hearingsService;

    @Mock
    private Gson gson;

    @Test
    public void givenHearingISFound_whenSchedulerIsCalledThenCCtProcessIsTriggered() {

        //given
        when(prosecutionConcludedRepository.getConcludedCases()).thenReturn(List.of(ProsecutionConcludedEntity
                .builder()
                .maatId(1234)
                .caseData("test".getBytes(StandardCharsets.UTF_8))
                .build()));

        when(gson.fromJson(new String("test".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8),
            ProsecutionConcluded.class)).thenReturn(new ProsecutionConcluded());

        when(hearingsService.retrieveHearingForCaseConclusion(any())).
                thenReturn(WQHearingEntity.builder().wqJurisdictionType("CROWN").build());

        //when
        prosecutionConcludedScheduler.process();

        //then
        verify(prosecutionConcludedService, atLeast(1)).executeCCOutCome(any(),any());
    }

    @Test
    public void givenHearingISNOTFound_whenSchedulerIsCalledThenCaseConclusionIsNotProcessIsTriggered() {

        //given
        when(prosecutionConcludedRepository.getConcludedCases()).thenReturn(List.of(ProsecutionConcludedEntity
                .builder()
                .maatId(1234)
                .caseData("hearingIdWhereChangeOccurred".getBytes(StandardCharsets.UTF_8))
                .build()));

        when(gson.fromJson(new String("hearingIdWhereChangeOccurred".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8),
            ProsecutionConcluded.class)).thenReturn(new ProsecutionConcluded());

        when(hearingsService.retrieveHearingForCaseConclusion(any())).
                thenReturn(null);
        //when
        prosecutionConcludedScheduler.process();

        //then
        verify(prosecutionConcludedService, never()).execute(any());
        verify(prosecutionConcludedRepository, never()).saveAll(any());
    }
}
