package gov.uk.courtdata.hearing.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQOffenceEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQOffenceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WQOffenceProcessorTest {

    @InjectMocks
    private WQOffenceProcessor wqOffenceProcessor;

    @Spy
    private WQOffenceRepository wqOffenceRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<WQOffenceEntity> wqOffenceEntityArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenOffenceProcessor_whenProcessIsInvoke_thenSaveOffence1() {

        //given
        HearingDTO hearingDTO =  testModelDataBuilder.getHearingDTO();

        //when
        wqOffenceProcessor.process(hearingDTO);

        //then
        verify(wqOffenceRepository).save(wqOffenceEntityArgumentCaptor.capture());

       assertThat(wqOffenceEntityArgumentCaptor.getValue().getTxId()).isEqualTo(123456);
       assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalAidStatus()).isEqualTo("Pending");
       assertThat(wqOffenceEntityArgumentCaptor.getValue().getLegalaidReason()).isEqualTo("some aid reason");
       assertThat(wqOffenceEntityArgumentCaptor.getValue().getAsnSeq()).isEqualTo("1");
    }
}
