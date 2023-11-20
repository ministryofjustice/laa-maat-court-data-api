package gov.uk.courtdata.eform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.repository.EformResultsRepository;
import gov.uk.courtdata.eform.repository.entity.EformResultsEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static gov.uk.courtdata.eform.controller.EformResultsControllerTest.buildEformResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EformResultsServiceTest {

    @InjectMocks
    private EformResultsService eformResultsService;
    @Mock
    private EformResultsRepository eformResultsRepository;

    @Test
    void shouldRetrieveEformResultForGivenUSN() {
        EformResultsEntity eformResultsEntity = buildEformResult();
        List<EformResultsEntity> eformResultsEntityList = new ArrayList<>();
        eformResultsEntityList.add(eformResultsEntity);
        when(eformResultsRepository.findAllByUsn(anyInt())).thenReturn(eformResultsEntityList);
        eformResultsService.getAllEformResults(anyInt());
        verify(eformResultsRepository, times(1)).findAllByUsn(anyInt());
    }

    @Test
    void shouldCreateEformResultRecordForGivenUSN() {
        EformResultsEntity eformResultsEntity = buildEformResult();
        eformResultsService.create(eformResultsEntity);
        verify(eformResultsRepository, times(1)).save(eformResultsEntity);
    }

    @Test
    void shouldDeleteEformResultRecordForGivenUSN() {
        eformResultsService.delete(anyInt());
        verify(eformResultsRepository, times(1)).deleteAllByUsn(anyInt());
    }

    @Test
    void shouldUpdateEformResultRecordForGivenUSN() throws JsonProcessingException {
        EformResultsEntity eformResultsEntity = EformResultsEntity.builder().iojResult("FAIL").build();
        when(eformResultsRepository.findTopByUsnOrderByIdDesc(anyInt())).thenReturn(eformResultsEntity);
        eformResultsService.updateEformResultFields(anyInt(), eformResultsEntity);
        verify(eformResultsRepository, times(1)).findTopByUsnOrderByIdDesc(anyInt());
        verify(eformResultsRepository, times(1)).save(eformResultsEntity);
    }

    @Test
    void shouldReturnDataNotFoundIfUsnNotThereInEformResultTable() throws JsonProcessingException {
        EformResultsEntity eformResultsEntity = EformResultsEntity.builder().iojResult("FAIL").build();
        when(eformResultsRepository.findTopByUsnOrderByIdDesc(1234)).thenReturn(null);
        UsnException exception = assertThrows(UsnException.class, () -> {
            eformResultsService.updateEformResultFields(1234, eformResultsEntity);
        });
        verify(eformResultsRepository, times(1)).findTopByUsnOrderByIdDesc(anyInt());
        verify(eformResultsRepository, times(0)).save(eformResultsEntity);
        assertEquals("The USN [1234] not found in Eform Results table", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpResponseCode());
    }
}