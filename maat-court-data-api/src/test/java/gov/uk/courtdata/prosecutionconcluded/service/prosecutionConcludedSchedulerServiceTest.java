package gov.uk.courtdata.prosecutionconcluded.service;

import com.google.gson.Gson;
import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import gov.uk.courtdata.repository.WQHearingRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class prosecutionConcludedSchedulerServiceTest {

    @InjectMocks
    private prosecutionConcludedSchedulerService prosecutionConcludedSchedulerService;
    @Mock
    private ProsecutionConcludedRepository prosecutionConcludedRepository;
    @Mock
    private ProsecutionConcludedService prosecutionConcludedService;
    @Mock
    private WQHearingRepository wqHearingRepository;

    @Mock
    private Gson gson;

    @Test
    public void test_process() {

        //given
        when(prosecutionConcludedRepository.getConcludedCases()).thenReturn(List.of(ProsecutionConcludedEntity
                .builder()
                .maatId(1234)
                .caseData("test".getBytes(StandardCharsets.UTF_8))
                .build()));
        when(wqHearingRepository.findByMaatIdAndHearingUUID(any(),any())).
                thenReturn(List.of(WQHearingEntity.builder().wqJurisdictionType("CROWN").build()));

        //when
        prosecutionConcludedSchedulerService.process();

        //then
        verify(prosecutionConcludedService, atLeast(1)).execute(any());
    }
}
