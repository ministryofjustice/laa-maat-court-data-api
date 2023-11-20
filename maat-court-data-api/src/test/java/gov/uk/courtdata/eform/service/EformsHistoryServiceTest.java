package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.eform.repository.EformsHistoryRepository;
import gov.uk.courtdata.eform.repository.entity.EformsHistory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EformsHistoryServiceTest {

    @InjectMocks
    private EformsHistoryService eformsHistoryService;
    @Mock
    private EformsHistoryRepository eformsHistoryRepository;

    @Test
    void shouldRetrieveEformDecisionHistoryForGivenUSN() {
        EformsHistory eformsHistory = EformsHistory.builder().id(1).usn(123).action("Get").userCreated("test").build();
        List<EformsHistory> eformsHistoryList = List.of(eformsHistory);
        when(eformsHistoryRepository.findAllByUsn(anyInt())).thenReturn(eformsHistoryList);
        eformsHistoryService.getAllEformsHistory(anyInt());
        verify(eformsHistoryRepository, times(1)).findAllByUsn(anyInt());
    }

    @Test
    void shouldRetrieveLatestEformDecisionHistoryForGivenUSN() {
        EformsHistory eformsHistory = EformsHistory.builder().id(1).usn(123).action("Get").userCreated("test").build();
        when(eformsHistoryRepository.findTopByUsnOrderByIdDesc(anyInt())).thenReturn(eformsHistory);
        eformsHistoryService.getLatestEformsHistoryRecord(anyInt());
        verify(eformsHistoryRepository, times(1)).findTopByUsnOrderByIdDesc(anyInt());
    }

    @Test
    void shouldCreateEformsDecisionHistoryRecordForGivenUSN() {
        EformsHistory eformsHistory = EformsHistory.builder().id(1).usn(123).action("Get").userCreated("test").build();
        eformsHistoryService.createEformsHistory(eformsHistory);
        verify(eformsHistoryRepository, times(1)).saveAndFlush(eformsHistory);
    }

    @Test
    void shouldDeleteEformsDecisionHistoryRecordForGivenUSN() {
        eformsHistoryService.deleteEformsHistory(anyInt());
        verify(eformsHistoryRepository, times(1)).deleteAllByUsn(anyInt());
    }

}
