package gov.uk.courtdata.link.processor;

import static gov.uk.courtdata.constants.CourtDataConstants.G_NO;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.ResultEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Result;
import gov.uk.courtdata.processor.ResultCodeRefDataProcessor;
import gov.uk.courtdata.repository.ResultRepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResultsInfoProcessorTest {

    @InjectMocks
    private ResultsInfoProcessor resultsInfoProcessor;

    @Spy
    private ResultRepository resultRepository;

    @Captor
    private ArgumentCaptor<List<ResultEntity>> resultsCaptor;

    @Mock
    private ResultCodeRefDataProcessor resultCodeRefDataProcessor;

    @Test
    void givenResultsModel_whenProcessIsInvoked_thenResultRecordIsCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        Result result =
                caseDetails.getDefendant().getOffences().getFirst().getResults().getFirst();

        // when
        resultsInfoProcessor.process(courtDataDTO);

        // then
        verify(resultRepository).saveAll(resultsCaptor.capture());
        ResultEntity firstResult = resultsCaptor.getValue().getFirst();

        assertThat(firstResult.getResultCode()).isEqualTo(result.getResultCode());
        assertThat(firstResult.getCaseId()).isEqualTo(courtDataDTO.getCaseId());
        assertThat(firstResult.getResultShortTitle()).isEqualTo(result.getResultShortTitle());
        assertThat(firstResult.getWqResult()).isEqualTo(G_NO);
    }

    @Test
    void givenResultsModelIsNULL_whenProcessIsInvoked_thenResultRecordIsNOTCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        caseDetails.getDefendant().getOffences().getFirst().setResults(null);
        // when
        resultsInfoProcessor.process(courtDataDTO);

        // then
        verify(resultRepository, times(0)).saveAll(anyCollection());
    }

    @Test
    void givenResultsModelIsEmpty_whenProcessIsInvoked_thenResultRecordIsNOTCreated() {

        // given
        CourtDataDTO courtDataDTO = TestModelDataBuilder.getCourtDataDTO();
        CaseDetails caseDetails = courtDataDTO.getCaseDetails();
        List<Result> resultList = new ArrayList<>();
        caseDetails.getDefendant().getOffences().getFirst().setResults(resultList);
        // when
        resultsInfoProcessor.process(courtDataDTO);

        // then
        verify(resultRepository, times(0)).saveAll(anyCollection());
    }
}
