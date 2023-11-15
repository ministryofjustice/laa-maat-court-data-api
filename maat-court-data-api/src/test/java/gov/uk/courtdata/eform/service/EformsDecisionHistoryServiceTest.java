package gov.uk.courtdata.eform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.eform.repository.EformsDecisionHistoryRepository;
import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EformsDecisionHistoryServiceTest {

    @InjectMocks
    private EformsDecisionHistoryService eformsDecisionHistoryService;
    @Mock
    private EformsDecisionHistoryRepository eformsDecisionHistoryRepository;

    @Test
    void shouldRetrieveEformDecisionHistoryForGivenUSN() {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(123).build();
        List<EformsDecisionHistory> eformsDecisionHistoryList = List.of(eformsDecisionHistory);
        when(eformsDecisionHistoryRepository.findAllByUsn(anyInt())).thenReturn(eformsDecisionHistoryList);
        eformsDecisionHistoryService.getAllEformsDecisionHistory(anyInt());
        verify(eformsDecisionHistoryRepository).findAllByUsn(anyInt());
    }

    @Test
    void shouldRetrieveLatestEformDecisionHistoryForGivenUSN() {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(123).build();
        when(eformsDecisionHistoryRepository.findTopByUsnOrderByIdDesc(anyInt())).thenReturn(eformsDecisionHistory);
        eformsDecisionHistoryService.getNewEformsDecisionHistoryRecord(anyInt());
        verify(eformsDecisionHistoryRepository).findTopByUsnOrderByIdDesc(anyInt());
    }

    @Test
    void shouldRetrievePreviousEformsDecisionHistoryRecordWroteToResultForGivenUSN() {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(123).wroteToResults("Y").build();
        when(eformsDecisionHistoryRepository.findFirstByUsnAndWroteToResultsOrderByIdDesc(123, "Y")).thenReturn(eformsDecisionHistory);
        eformsDecisionHistoryService.getPreviousEformsDecisionHistoryRecordWroteToResult(123);
        verify(eformsDecisionHistoryRepository).findFirstByUsnAndWroteToResultsOrderByIdDesc(123, "Y");
    }

    @Test
    void shouldCreateEformsDecisionHistoryRecordForGivenUSN() {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(123).build();
        eformsDecisionHistoryService.createEformsDecisionHistory(eformsDecisionHistory);
        verify(eformsDecisionHistoryRepository).saveAndFlush(eformsDecisionHistory);
    }

    @Test
    void shouldDeleteEformsDecisionHistoryRecordForGivenUSN() {
        eformsDecisionHistoryService.deleteEformsDecisionHistory(anyInt());
        verify(eformsDecisionHistoryRepository).deleteAllByUsn(anyInt());
    }

    @Test
    void shouldUpdateEformsDecisionHistoryRecordForGivenUSN() throws JsonProcessingException {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(123).build();
        String requestJson = "{\"wroteToResults\":\"Y\"}";
        Map<String, Object> updateFields = new ObjectMapper().readValue(requestJson, HashMap.class);
        when(eformsDecisionHistoryRepository.findTopByUsnOrderByIdDesc(anyInt())).thenReturn(eformsDecisionHistory);
        eformsDecisionHistoryService.updateEformsDecisionHistoryFields(anyInt(), updateFields);
        verify(eformsDecisionHistoryRepository).findTopByUsnOrderByIdDesc(anyInt());
        verify(eformsDecisionHistoryRepository).save(eformsDecisionHistory);
    }
}
