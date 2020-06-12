package gov.uk.courtdata.hearing.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQDefendant;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQDefendantRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WQDefendantProcessorTest {

    @InjectMocks
    private WQDefendantProcessor wqDefendantProcessor;

    @Spy
    private WQDefendantRepository defendantRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<WQDefendant> wqDefendantArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenDefendantProcessor_whenProcessIsInvoke_thenSaveDefendant() {
        //given
        HearingDTO hearingDTO =  testModelDataBuilder.getHearingDTO();

        //when
        wqDefendantProcessor.process(hearingDTO);

        //then
        verify(defendantRepository).save(wqDefendantArgumentCaptor.capture());
        assertThat(wqDefendantArgumentCaptor.getValue().getSurname()).isEqualTo("Smith");
        assertThat(wqDefendantArgumentCaptor.getValue().getPostCode()).isEqualTo("LU3 111");
    }
}
