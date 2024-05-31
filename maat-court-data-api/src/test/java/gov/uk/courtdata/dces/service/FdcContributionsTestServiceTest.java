package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.request.CreateFdcTestDataRequest;
import gov.uk.courtdata.dces.request.FdcNegativeTestType;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import gov.uk.courtdata.repository.FdcItemsRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FdcContributionsTestServiceTest {

    @InjectMocks
    FdcContributionsTestService fdcContributionsTestService;
    @Mock
    private DebtCollectionRepository debtCollectionRepository;
    @Mock
    private FdcItemsRepository fdcItemsRepository;
    @Mock
    private RepOrderRepository repOrderRepository;
    @Mock
    private CrownCourtProcessingRepository repOrderCrownCourtOutcomeRepository;
    @Mock
    private FdcContributionsRepository fdcContributionsRepository;

    @Captor
    ArgumentCaptor<List<RepOrderEntity>> repOrderEntityListCaptor;

    @Captor
    ArgumentCaptor<List<FdcContributionsEntity>> fdcContributionEntityListCaptor;

    @Test
    void testPositiveTestDataGeneration(){
        when(fdcContributionsRepository.save(any(FdcContributionsEntity.class))).thenAnswer(mockedSaveFdcContributions());
        when(debtCollectionRepository.getMerge1TestCandidates(5)).thenReturn(new ArrayList<>(List.of(1,2,3,4,5)));
        fdcContributionsTestService.createFdcMergeTestData(createTestDataRequest(5, false, null));
        verify(debtCollectionRepository).getMerge1TestCandidates(5);
        verify(fdcContributionsRepository, times(5)).save(any());
        verify(repOrderRepository, never()).saveAll(anyList());
        verify(fdcItemsRepository, never()).deleteAllByFdcIdIn(anyList());
        verify(fdcItemsRepository, times(5)).save(any());
        verify(fdcContributionsRepository, never()).saveAll(anyList());
        verify(repOrderCrownCourtOutcomeRepository, never()).deleteAllByRepOrder_IdIn(anyList());
    }

    @Test
    void testSODNegativeTestDataGeneration(){
        LocalDate oldDate = LocalDate.of(1998,1,1);
        List<RepOrderEntity> repOrderEntityList = List.of(RepOrderEntity.builder().sentenceOrderDate(oldDate).build());


        when(debtCollectionRepository.getMerge1TestCandidates(5)).thenReturn(new ArrayList<>(List.of(1,2,3,4,5)));
        when(repOrderRepository.findByIdIn(anyList())).thenReturn(repOrderEntityList);
        when(fdcContributionsRepository.save(any(FdcContributionsEntity.class))).thenAnswer(mockedSaveFdcContributions());
        fdcContributionsTestService.createFdcMergeTestData(createTestDataRequest(5, true, FdcNegativeTestType.SOD));
        verify(debtCollectionRepository).getMerge1TestCandidates(5);
        verify(repOrderRepository, times(1)).saveAll(repOrderEntityListCaptor.capture());
        verify(fdcContributionsRepository, times(5)).save(any());
        verify(fdcItemsRepository, never()).deleteAllByFdcIdIn(anyList());
        verify(fdcItemsRepository, times(5)).save(any());
        verify(fdcContributionsRepository, never()).saveAll(anyList());
        verify(repOrderCrownCourtOutcomeRepository, never()).deleteAllByRepOrder_IdIn(anyList());
        // verify SOD test has modified the date to be now + 3 months.
        LocalDate expectedDate = LocalDate.now().plus(3, ChronoUnit.MONTHS);
        List<RepOrderEntity> savedRepOrders = repOrderEntityListCaptor.getValue();
        assertTrue(Objects.nonNull(savedRepOrders));
        assertEquals(1, savedRepOrders.size());
        assertEquals(expectedDate, savedRepOrders.get(0).getSentenceOrderDate());
    }

    @Test
    void testCCONegativeTestDataGeneration(){
        when(debtCollectionRepository.getMerge1TestCandidates(5)).thenReturn(new ArrayList<>(List.of(1,2,3,4,5)));
        when(fdcContributionsRepository.save(any(FdcContributionsEntity.class))).thenAnswer(mockedSaveFdcContributions());
        fdcContributionsTestService.createFdcMergeTestData(createTestDataRequest(5, true, FdcNegativeTestType.CCO));
        verify(debtCollectionRepository).getMerge1TestCandidates(5);
        verify(fdcContributionsRepository, times(5)).save(any());
        verify(repOrderRepository, never()).saveAll(anyList());
        verify(fdcItemsRepository, never()).deleteAllByFdcIdIn(anyList());
        verify(fdcItemsRepository, times(5)).save(any());
        verify(fdcContributionsRepository, never()).saveAll(anyList());
        verify(repOrderCrownCourtOutcomeRepository, times(1)).deleteAllByRepOrder_IdIn(anyList());
    }

    @Test
    void testFdcStatusNegativeTestDataGeneration(){
        List<FdcContributionsEntity> fdcContributionsEntityList = List.of(FdcContributionsEntity.builder().status(FdcContributionsStatus.REPLACED).build());
        when(debtCollectionRepository.getMerge1TestCandidates(5)).thenReturn(new ArrayList<>(List.of(1,2,3,4,5)));
        when(fdcContributionsRepository.findByRepOrderEntity_IdIn(any())).thenReturn(fdcContributionsEntityList);
        when(fdcContributionsRepository.save(any(FdcContributionsEntity.class))).thenAnswer(mockedSaveFdcContributions());
        fdcContributionsTestService.createFdcMergeTestData(createTestDataRequest(5, true, FdcNegativeTestType.FDC_STATUS));
        verify(debtCollectionRepository).getMerge1TestCandidates(5);
        verify(fdcContributionsRepository, times(5)).save(any());
        verify(fdcContributionsRepository, times(1)).saveAll(fdcContributionEntityListCaptor.capture());
        verify(repOrderRepository, never()).saveAll(anyList());
        verify(fdcItemsRepository, never()).deleteAllByFdcIdIn(anyList());
        verify(fdcItemsRepository, times(5)).save(any());
        verify(repOrderCrownCourtOutcomeRepository, never()).deleteAllByRepOrder_IdIn(anyList());

        List<FdcContributionsEntity> savedFdcEntities = fdcContributionEntityListCaptor.getValue();
        assertTrue(Objects.nonNull(savedFdcEntities));
        assertEquals(1, savedFdcEntities.size());
        assertEquals(FdcContributionsStatus.SENT, savedFdcEntities.get(0).getStatus());
    }

    @Test
    void testFdcItemNegativeTestDataGeneration(){
        when(debtCollectionRepository.getMerge1TestCandidates(5)).thenReturn(new ArrayList<>(List.of(1,2,3,4,5)));
        when(fdcContributionsRepository.save(any(FdcContributionsEntity.class))).thenAnswer(mockedSaveFdcContributions());
        fdcContributionsTestService.createFdcMergeTestData(createTestDataRequest(5, true, FdcNegativeTestType.FDC_ITEM));
        verify(debtCollectionRepository).getMerge1TestCandidates(5);
        verify(fdcContributionsRepository, times(5)).save(any());
        verify(repOrderRepository, never()).saveAll(anyList());
        verify(fdcItemsRepository, times(1)).deleteAllByFdcIdIn(anyList());
        verify(fdcItemsRepository, never()).save(any());
        verify(fdcContributionsRepository, never()).saveAll(anyList());
        verify(repOrderCrownCourtOutcomeRepository, never()).deleteAllByRepOrder_IdIn(anyList());
    }

    private CreateFdcTestDataRequest createTestDataRequest(int numTestEntries, boolean isNegativeTest, FdcNegativeTestType testType){
        return CreateFdcTestDataRequest.builder()
                .numOfTestEntries(numTestEntries)
                .negativeTest(isNegativeTest)
                .negativeTestType(testType)
                .build();
    }

    private Answer<Void> mockedSaveFdcContributions() {
        return invocation -> {
            FdcContributionsEntity entity = invocation.getArgument(0);
            entity.setId(entity.getRepOrderEntity().getId());
            return null;
        };
    }

}