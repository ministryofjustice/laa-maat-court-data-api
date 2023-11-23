package gov.uk.courtdata.eform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.repository.EformsDecisionHistoryRepository;
import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

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
        verify(eformsDecisionHistoryRepository, times(1)).findAllByUsn(anyInt());
    }

    @Test
    void shouldRetrieveLatestEformDecisionHistoryForGivenUSN() {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(123).build();
        when(eformsDecisionHistoryRepository.findTopByUsnOrderByIdDesc(anyInt())).thenReturn(eformsDecisionHistory);
        eformsDecisionHistoryService.getNewEformsDecisionHistoryRecord(anyInt());
        verify(eformsDecisionHistoryRepository, times(1)).findTopByUsnOrderByIdDesc(anyInt());
    }

    @Test
    void shouldRetrievePreviousEformsDecisionHistoryRecordWroteToResultForGivenUSN() {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(123).wroteToResults("Y").build();
        when(eformsDecisionHistoryRepository.findFirstByUsnAndWroteToResultsOrderByIdDesc(123, "Y")).thenReturn(eformsDecisionHistory);
        eformsDecisionHistoryService.getPreviousEformsDecisionHistoryRecordWroteToResult(123);
        verify(eformsDecisionHistoryRepository, times(1)).findFirstByUsnAndWroteToResultsOrderByIdDesc(123, "Y");
    }

    @Test
    void shouldCreateEformsDecisionHistoryRecordForGivenUSN() {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().id(1).usn(123).build();
        eformsDecisionHistoryService.createEformsDecisionHistory(eformsDecisionHistory);
        verify(eformsDecisionHistoryRepository, times(1)).saveAndFlush(eformsDecisionHistory);
    }

    @Test
    void shouldDeleteEformsDecisionHistoryRecordForGivenUSN() {
        eformsDecisionHistoryService.deleteEformsDecisionHistory(anyInt());
        verify(eformsDecisionHistoryRepository, times(1)).deleteAllByUsn(anyInt());
    }

    @Test
    void shouldUpdateEformsDecisionHistoryRecordForGivenUSN() throws JsonProcessingException {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().wroteToResults("Y").build();
        when(eformsDecisionHistoryRepository.findTopByUsnOrderByIdDesc(anyInt())).thenReturn(eformsDecisionHistory);
        eformsDecisionHistoryService.updateEformsDecisionHistoryFields(anyInt(), eformsDecisionHistory);
        verify(eformsDecisionHistoryRepository, times(1)).findTopByUsnOrderByIdDesc(anyInt());
        verify(eformsDecisionHistoryRepository, times(1)).save(eformsDecisionHistory);
    }

    @Test
    void shouldReturnDataNotFoundIfUsnNotThereInEformsDecisionHistoryTable() throws JsonProcessingException {
        EformsDecisionHistory eformsDecisionHistory = EformsDecisionHistory.builder().wroteToResults("Y").build();
        when(eformsDecisionHistoryRepository.findTopByUsnOrderByIdDesc(1234)).thenReturn(null);
        UsnException exception = assertThrows(UsnException.class, () -> {
            eformsDecisionHistoryService.updateEformsDecisionHistoryFields(1234, eformsDecisionHistory);
        });
        verify(eformsDecisionHistoryRepository, times(1)).findTopByUsnOrderByIdDesc(anyInt());
        verify(eformsDecisionHistoryRepository, times(0)).save(eformsDecisionHistory);
        assertEquals("The USN [1234] not found in Eforms Decision History table", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpResponseCode());
    }
}
