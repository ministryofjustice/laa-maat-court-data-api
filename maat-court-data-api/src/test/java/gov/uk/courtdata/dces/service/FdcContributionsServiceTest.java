package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.dces.response.FdcContributionsGlobalUpdateResponse;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static gov.uk.courtdata.enums.FdcContributionsStatus.REQUESTED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FdcContributionsServiceTest {

    private List<FdcContributionsEntity> fdcContributionsEntityList;

    @InjectMocks
    private FdcContributionsService fdcContributionsService;

    @Mock
    private DebtCollectionRepository debtCollectionRepository;
    @Mock
    private FdcContributionsRepository fdcContributionsRepository;

    private LocalDate expectedDateCalculated;
    private LocalDate expectedSentenceDate;
    private final BigDecimal expectedAgfsCost = new BigDecimal("100.50");
    private final BigDecimal expectedLgfsCost = new BigDecimal("333.33");
    private final BigDecimal expectedFinalCost = new BigDecimal("1010.10");
    private final Integer expectedId = 111;
    @Captor
    private ArgumentCaptor<FdcContributionsStatus> statusCaptor;

    @BeforeEach
    void setUp() {
        expectedDateCalculated = LocalDate.now();
        expectedSentenceDate = LocalDate.now().minusDays(1);
        getFdcContributionsFile();
    }

    @Test
    void testGetContributionFilesWhenFdcFileStatusIsRequested() {
        when(fdcContributionsRepository.findByStatus(statusCaptor.capture())).thenReturn(fdcContributionsEntityList);
        FdcContributionsResponse response = fdcContributionsService.getFdcContributionFiles(REQUESTED);

        assertNotNull(response);

        List<FdcContributionEntry> fdcContributionEntries = response.getFdcContributions();
        Assertions.assertFalse(fdcContributionEntries.isEmpty());
        final FdcContributionsStatus capturedStatus = statusCaptor.getValue();
        assertEquals(REQUESTED, capturedStatus);
        FdcContributionEntry responseValue = fdcContributionEntries.get(0);
        assertEquals(expectedId, responseValue.getId());
        assertEquals(expectedFinalCost,responseValue.getFinalCost());
        assertEquals(expectedAgfsCost,responseValue.getAgfsCost());
        assertEquals(expectedLgfsCost,responseValue.getLgfsCost());
        assertEquals(expectedDateCalculated,responseValue.getDateCalculated());
        assertEquals(expectedSentenceDate,responseValue.getSentenceOrderDate());
    }

    @Test
    void testGlobalUpdateIsSuccess() {
        int expected1 = 2;
        int expected2 = 5;

        when(debtCollectionRepository.globalUpdatePart1()).thenReturn(new int[]{expected1});
        when(debtCollectionRepository.globalUpdatePart2()).thenReturn(new int[]{expected2});
        FdcContributionsGlobalUpdateResponse response = fdcContributionsService.fdcContributionGlobalUpdate();
        assertNotNull(response);
        assertEquals(expected1+expected2, response.getResponses());
    }


    private void getFdcContributionsFile() {
        fdcContributionsEntityList = new ArrayList<>();
        RepOrderEntity repOrderEntity = new RepOrderEntity();
        repOrderEntity.setSentenceOrderDate(expectedSentenceDate);
        FdcContributionsEntity fdcFile = new FdcContributionsEntity();
        fdcFile.setStatus(REQUESTED);
        fdcFile.setId(expectedId);
        fdcFile.setAgfsCost(expectedAgfsCost);
        fdcFile.setLgfsCost(expectedLgfsCost);
        fdcFile.setFinalCost(expectedFinalCost);
        fdcFile.setDateCalculated(expectedDateCalculated);
        fdcFile.setRepOrderEntity(repOrderEntity);
        fdcContributionsEntityList.add(fdcFile);
    }
}
