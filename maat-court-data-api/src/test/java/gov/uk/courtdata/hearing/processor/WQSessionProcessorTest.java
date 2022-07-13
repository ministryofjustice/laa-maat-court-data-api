package gov.uk.courtdata.hearing.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQSessionEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WQSessionProcessorTest {

    @InjectMocks
    private WQSessionProcessor wqSessionProcessor;

    @Spy
    private WQSessionRepository wqSessionRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    ArgumentCaptor<WQSessionEntity> wqSessionEntityArgumentCaptor;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenCaseProcessor_whenProcessIsInvoke_thenSaveCase() {
        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();

        //when
        wqSessionProcessor.process(hearingDTO);

        verify(wqSessionRepository).save(wqSessionEntityArgumentCaptor.capture());
        assertThat(wqSessionEntityArgumentCaptor.getValue().getCaseId()).isEqualTo(1234);
        assertThat(wqSessionEntityArgumentCaptor.getValue().getCourtLocation()).isEqualTo("London");
        assertThat(wqSessionEntityArgumentCaptor.getValue().getDateOfHearing()).isEqualTo("2020-08-16");
        assertThat(wqSessionEntityArgumentCaptor.getValue().getSessionvalidatedate()).isEqualTo("2020-08-16");

    }

}
