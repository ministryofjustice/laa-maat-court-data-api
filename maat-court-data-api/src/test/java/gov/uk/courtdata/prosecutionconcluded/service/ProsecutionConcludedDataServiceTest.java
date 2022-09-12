package gov.uk.courtdata.prosecutionconcluded.service;


import com.google.gson.Gson;
import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProsecutionConcludedDataServiceTest {

    @InjectMocks
    private ProsecutionConcludedDataService prosecutionConcludedDataService;

    @Mock
    private ProsecutionConcludedRepository prosecutionConcludedRepository;
    @Mock
    private Gson gson;

    @Test
    public void givenHearingDataAlreadyExistsWhenExecuteIsCalledThenDataUpdated() {


        when(prosecutionConcludedRepository.getByMaatId(any()))
                .thenReturn(List.of(ProsecutionConcludedEntity.builder().maatId(1234).retryCount(0).build()));
        //given
        prosecutionConcludedDataService.execute(ProsecutionConcluded.
                builder()
                .hearingIdWhereChangeOccurred(UUID.randomUUID())
                .build());

        //then
        verify(prosecutionConcludedRepository, atLeast(1)).saveAll(any());

    }

    @Test
    public void givenHearingDataDoesNotExistWhenExecuteIsCalledThenDataCreated() {


        when(prosecutionConcludedRepository.getByMaatId(any()))
                .thenReturn(new ArrayList<>());
        //given
        prosecutionConcludedDataService.execute(ProsecutionConcluded.
                builder()
                .hearingIdWhereChangeOccurred(UUID.randomUUID())
                .build());

        //then
        verify(prosecutionConcludedRepository, atLeast(1)).save(any());

    }

    @Test
    public void test_whenUpdateConclusionIsCalledThenDataSaved() {

        //when
        when(prosecutionConcludedRepository.getByMaatId(1234)).thenReturn(List.of(ProsecutionConcludedEntity.builder().build()));

        //given
        prosecutionConcludedDataService.updateConclusion(1234);

        //then
        verify(prosecutionConcludedRepository, atLeast(1)).saveAll(any());

    }
}
