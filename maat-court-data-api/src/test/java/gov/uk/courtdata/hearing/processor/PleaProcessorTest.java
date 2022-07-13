package gov.uk.courtdata.hearing.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.PleaEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.PleaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PleaProcessorTest {

    @InjectMocks
    private PleaProcessor pleaProcessor;

    @Spy
    private PleaRepository pleaRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    ArgumentCaptor<PleaEntity> pleaEntityArgumentCaptor;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenCaseProcessor_whenProcessIsInvoke_thenSavePlea() {
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTOForCCOutcome();

        //when
        pleaProcessor.process(hearingDTO);
        verify(pleaRepository).save(pleaEntityArgumentCaptor.capture());
        assertThat(pleaEntityArgumentCaptor.getValue().getPleaValue()).isEqualTo("NOT_GUILTY");
        assertThat(pleaEntityArgumentCaptor.getValue().getPleaDate()).isEqualTo("2020-10-12");
        assertThat(pleaEntityArgumentCaptor.getValue().getOffenceId()).isEqualTo("123456");
        assertThat(pleaEntityArgumentCaptor.getValue().getMaatId()).isEqualTo(789034);
    }

    @Test
    public void givenCaseProcessor_whenPleaIsNull_thenSavePlea() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            //when
            pleaProcessor.process(HearingDTO.builder().build());
            verify(pleaRepository, times(10)).save(any());
        });
    }
}