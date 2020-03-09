package gov.uk.courtdata.link.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.WqCoreEntity;
import gov.uk.courtdata.repository.WqCoreRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static gov.uk.courtdata.constants.CourtDataConstants.WQ_CREATION_EVENT;
import static gov.uk.courtdata.constants.CourtDataConstants.WQ_SUCCESS_STATUS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WqCoreInfoProcessorTest {

    @InjectMocks
    private WqCoreInfoProcessor wqCoreInfoProcessor;
   @Spy
    private WqCoreRepository wqCoreRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<WqCoreEntity> wqCoreCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenWQCoreModel_whenProcessIsInvoked_thenWQCoreRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();

        // when
        wqCoreInfoProcessor.process(courtDataDTO);

        // then
        verify(wqCoreRepository).save(wqCoreCaptor.capture());
        assertThat(wqCoreCaptor.getValue().getTxId()).isEqualTo(courtDataDTO.getTxId());
        assertThat(wqCoreCaptor.getValue().getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(wqCoreCaptor.getValue().getWqStatus()).isEqualTo(WQ_SUCCESS_STATUS);
        assertThat(wqCoreCaptor.getValue().getWqType()).isEqualTo(WQ_CREATION_EVENT);
    }
}
