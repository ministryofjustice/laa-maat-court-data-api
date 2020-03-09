package gov.uk.courtdata.link.processor;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.ResultEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.repository.ResultRepository;
import gov.uk.courtdata.util.CourtDataUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ResultsInfoProcessorTest {


    @InjectMocks
    private ResultsInfoProcessor resultsInfoProcessor;
    @Spy
    private ResultRepository resultRepository;

    private TestModelDataBuilder testModelDataBuilder;

    @Captor
    private ArgumentCaptor<List<ResultEntity>> resultsCaptor;

    @Mock
    private CourtDataUtil courtDataUtil;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        testModelDataBuilder = new TestModelDataBuilder(new TestEntityDataBuilder(), new Gson());
    }

    @Test
    public void givenResultsModel_whenProcessIsInvoked_thenResultRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = testModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        Result result = caseDetails.getDefendant().getOffences().get(0).getResults().get(0);

        // when
        resultsInfoProcessor.process(courtDataDTO);

        // then
        verify(resultRepository).saveAll(resultsCaptor.capture());
        assertThat(resultsCaptor.getValue().get(0).getResultCode()).isEqualTo(result.getResultCode());
        assertThat(resultsCaptor.getValue().get(0).getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(resultsCaptor.getValue().get(0).getResultShortTitle()).isEqualTo(result.getResultShortTitle());
        assertThat(resultsCaptor.getValue().get(0).getWqResult()).isEqualTo(G_NO);


    }
}
