package gov.uk.courtdata.hearing.crowncourt.impl;

import gov.uk.courtdata.entity.OffenceEntity;
import gov.uk.courtdata.entity.WqLinkRegisterEntity;
import gov.uk.courtdata.entity.XLATResultEntity;
import gov.uk.courtdata.model.Offence;
import gov.uk.courtdata.repository.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.TestComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OffenceHelperTest {
    @Rule
    public ExpectedException thrown = none();

    @InjectMocks
    private OffenceHelper offenceHelper;

    @Mock
    private WqLinkRegisterRepository wqLinkRegisterRepository;
    @Mock
    private OffenceRepository offenceRepository;

    @Mock
    private PleaRepository pleaRepository;
    @Mock
    private VerdictRepository verdictRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenMaatId_whenCalledGetOffences_thenReturnOffenceList() {

        when(wqLinkRegisterRepository.findBymaatId(anyInt()))
                .thenReturn(
                        Arrays.asList(WqLinkRegisterEntity.builder().caseId(121).build())
                );

        when(offenceRepository.findByCaseId(anyInt()))
                .thenReturn(
                        Arrays.asList(OffenceEntity.builder().caseId(121).offenceId("2222").build(),
                        OffenceEntity.builder().caseId(121).offenceId("33333").build())
                );


        List<Offence> offenceList = offenceHelper.getOffences(anyInt());

        assertThat(offenceList.get(0).getOffenceId()).isEqualTo("2222");
        verify(pleaRepository,atLeast(2)).getLatestPleaByOffence(anyString());
        verify(verdictRepository,atLeast(2)).getLatestVerdictByOffence(anyString());
    }

    @Test
    public void givenMaatId_whenCalledGetOffences_thenReturnOffenceListNull() {

        when(wqLinkRegisterRepository.findBymaatId(anyInt()))
                .thenReturn(
                        Arrays.asList(WqLinkRegisterEntity.builder().caseId(121).build())
                );

        when(offenceRepository.findByCaseId(anyInt())).thenReturn(Collections.emptyList());

        List<Offence> offenceList = offenceHelper.getOffences(anyInt());

        org.hamcrest.MatcherAssert.assertThat(offenceList, hasSize(0));
    }
}