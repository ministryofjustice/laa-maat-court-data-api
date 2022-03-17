package gov.uk.courtdata.prosecutionconcluded.service;


import com.google.gson.Gson;
import gov.uk.courtdata.entity.ProsecutionConcludedEntity;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.repository.ProsecutionConcludedRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProsecutionConcludedDataServiceTest {

    @InjectMocks
    private ProsecutionConcludedDataService prosecutionConcludedDataService;

    @Mock
    private ProsecutionConcludedRepository prosecutionConcludedRepository;
    @Mock
    private Gson gson;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_whenExecuteIsCalledThenDataSaved() {



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

        //given
        when(prosecutionConcludedRepository.getByMaatId(1234)).thenReturn(List.of(ProsecutionConcludedEntity.builder().build()));

        //when
        prosecutionConcludedDataService.updateConclusion(1234);

        //then
        verify(prosecutionConcludedRepository, atLeast(1)).saveAll(any());

    }
}
