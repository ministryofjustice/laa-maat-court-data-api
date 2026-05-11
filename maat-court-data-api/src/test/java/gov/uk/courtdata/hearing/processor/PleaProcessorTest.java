package gov.uk.courtdata.hearing.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.PleaEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.PleaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PleaProcessorTest {

    @InjectMocks
    private PleaProcessor pleaProcessor;

    @Spy
    private PleaRepository pleaRepository;

    @Captor
    ArgumentCaptor<PleaEntity> pleaEntityArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenCaseProcessor_whenProcessIsInvoke_thenSavePlea() {
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTOForCCOutcome();

        // when
        pleaProcessor.process(hearingDTO);
        verify(pleaRepository).save(pleaEntityArgumentCaptor.capture());
        PleaEntity entity = pleaEntityArgumentCaptor.getValue();
        assertThat(entity.getPleaValue()).isEqualTo("NOT_GUILTY");
        assertThat(entity.getPleaDate()).isEqualTo("2020-10-12");
        assertThat(entity.getOffenceId()).isEqualTo("123456");
        assertThat(entity.getMaatId()).isEqualTo(789034);
    }

    @Test
    void givenCaseProcessor_whenPleaIsNull_thenSavePlea() {
        HearingDTO hearingDto = HearingDTO.builder().build();
        assertThatThrownBy(() -> pleaProcessor.process(hearingDto)).isInstanceOf(NullPointerException.class);
        verify(pleaRepository, never()).save(any());
    }
}
