package gov.uk.courtdata.prosecutionconcluded.service;

import com.google.gson.Gson;
import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import gov.uk.courtdata.entity.WQHearingEntity;
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
    private prosecutionConcludedScheduler prosecutionConcludedScheduler;
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
        WQHearingEntity wqHearingEntity = WQHearingEntity.builder().wqJurisdictionType("CROWN").build();
        when(hearingsService.retrieveHearingForCaseConclusion(any())).
                thenReturn(wqHearingEntity);
        when(hearingsService.getWqHearingEntity(any())).
                thenReturn(wqHearingEntity);

        //when
        prosecutionConcludedScheduler.process();

        //then
        verify(prosecutionConcludedService, atLeast(1)).execute(any());
    }

    @Test
    public void givenHearingISNOTFound_whenSchedulerIsCalledThenCaseConclusionIsNotProcessIsTriggered() {

        //given
        when(prosecutionConcludedRepository.getConcludedCases()).thenReturn(List.of(ProsecutionConcludedEntity
                .builder()
                .maatId(1234)
                .caseData("hearingIdWhereChangeOccurred".getBytes(StandardCharsets.UTF_8))
                .build()));

        when(hearingsService.retrieveHearingForCaseConclusion(any())).
                thenReturn(null);
        //when
        prosecutionConcludedScheduler.process();

        //then
        verify(prosecutionConcludedService,never()).execute(any());
        verify(prosecutionConcludedRepository,never()).saveAll(any());
    }
}
