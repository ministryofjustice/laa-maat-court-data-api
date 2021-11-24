package gov.uk.courtdata.hearing.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.WQHearingEntity;
import gov.uk.courtdata.hearing.dto.HearingDTO;
import gov.uk.courtdata.repository.WQHearingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WQHearingProcessorTest {

    @InjectMocks
    private WQHearingProcessor wqHearingProcessor;

    @Spy
    private WQHearingRepository wqHearingRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<WQHearingEntity> wqHearingEntityArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenWQHearingProcessor_whenProcessIsInvoke_thenSaveWQHearingEntity() {
        //given
        HearingDTO hearingDTO =  testModelDataBuilder.getHearingDTO();

        //when
        wqHearingProcessor.process(hearingDTO);

        //then
        verify(wqHearingRepository).save(wqHearingEntityArgumentCaptor.capture());
        assertThat(wqHearingEntityArgumentCaptor.getValue().getHearingUUID()).isEqualTo(TestModelDataBuilder.HEARING_ID.toString());
        assertThat(wqHearingEntityArgumentCaptor.getValue().getMaatId()).isEqualTo(TestModelDataBuilder.MAAT_ID);
        assertThat(wqHearingEntityArgumentCaptor.getValue().getWqJurisdictionType()).isEqualTo(TestModelDataBuilder.JURISDICTION_TYPE_MAGISTRATES.toString());
        assertThat(wqHearingEntityArgumentCaptor.getValue().getOuCourtLocation()).isEqualTo(TestModelDataBuilder.COURT_LOCATION);
    }
}