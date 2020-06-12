package gov.uk.courtdata.hearing.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQResultEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQResultRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WQResultProcessorTest {

    @InjectMocks
    private WQResultProcessor wqResultProcessor;

    @Spy
    private WQResultRepository wqResultRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    ArgumentCaptor<WQResultEntity> wqResultEntityArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenCaseProcessor_whenProcessIsInvoke_thenSaveCase() {
        //given
        HearingDTO hearingDTO = testModelDataBuilder.getHearingDTO();

        //when
        wqResultProcessor.process(hearingDTO);

        //then
        verify(wqResultRepository).save(wqResultEntityArgumentCaptor.capture());
        assertThat(wqResultEntityArgumentCaptor.getValue().getCaseId()).isEqualTo(1234);
        assertThat(wqResultEntityArgumentCaptor.getValue().getResultCode()).isEqualTo(6666);
        assertThat(wqResultEntityArgumentCaptor.getValue().getResultText()).isEqualTo("This is a some result text for hearing");
        assertThat(wqResultEntityArgumentCaptor.getValue().getNextHearingLocation()).isEqualTo("London");
        assertThat(wqResultEntityArgumentCaptor.getValue().getFirmName()).isEqualTo("Bristol Law Service");
        assertThat(wqResultEntityArgumentCaptor.getValue().getResultShortTitle()).isEqualTo("Next call");

    }
}
