package gov.uk.courtdata.hearing.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQSessionEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQSessionRepository;

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
class WQSessionProcessorTest {

    @InjectMocks
    private WQSessionProcessor wqSessionProcessor;

    @Spy
    private WQSessionRepository wqSessionRepository;

    @Captor
    ArgumentCaptor<WQSessionEntity> wqSessionEntityArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenCaseProcessor_whenProcessIsInvoke_thenSaveCase() {
        // given
        HearingDTO hearingDTO = TestModelDataBuilder.getHearingDTO();

        // when
        wqSessionProcessor.process(hearingDTO);

        verify(wqSessionRepository).save(wqSessionEntityArgumentCaptor.capture());
        assertThat(wqSessionEntityArgumentCaptor.getValue().getCaseId()).isEqualTo(1234);
        assertThat(wqSessionEntityArgumentCaptor.getValue().getCourtLocation()).isEqualTo("London");
        assertThat(wqSessionEntityArgumentCaptor.getValue().getDateOfHearing()).isEqualTo("2020-08-16");
        assertThat(wqSessionEntityArgumentCaptor.getValue().getSessionvalidatedate())
                .isEqualTo("2020-08-16");
    }
}
