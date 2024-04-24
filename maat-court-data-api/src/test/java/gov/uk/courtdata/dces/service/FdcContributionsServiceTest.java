package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.request.CreateFdcFileRequest;
import gov.uk.courtdata.dces.request.LogFdcProcessedRequest;
import gov.uk.courtdata.dces.response.FdcContributionEntry;
import gov.uk.courtdata.dces.response.FdcContributionsGlobalUpdateResponse;
import gov.uk.courtdata.dces.response.FdcContributionsResponse;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static gov.uk.courtdata.enums.FdcContributionsStatus.REQUESTED;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FdcContributionsServiceTest {

    private List<FdcContributionsEntity> fdcContributionsEntityList;

    @InjectMocks
    private FdcContributionsService fdcContributionsService;

    @Mock
    private ContributionFileMapper contributionFileMapper;
    @Mock
    private DebtCollectionRepository debtCollectionRepository;
    @Mock
    private DebtCollectionService debtCollectionService;
    @Mock
    private FdcContributionsRepository fdcContributionsRepository;
    @Mock
    private ContributionFilesRepository contributionFilesRepository;
    @Mock
    private ContributionFileErrorsRepository contributionFileErrorsRepository;

    private LocalDate expectedDateCalculated;
    private LocalDate expectedSentenceDate;
    private final BigDecimal expectedAgfsCost = new BigDecimal("100.50");
    private final BigDecimal expectedLgfsCost = new BigDecimal("333.33");
    private final BigDecimal expectedFinalCost = new BigDecimal("1010.10");
    private final Integer expectedId = 111;
    @Captor
    private ArgumentCaptor<FdcContributionsStatus> statusCaptor;
    @Captor
    private ArgumentCaptor<ContributionFileErrorsEntity> fileErrorCaptor;

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

        when(debtCollectionRepository.globalUpdatePart1("5")).thenReturn(expected1);
        when(debtCollectionRepository.globalUpdatePart2("5")).thenReturn(expected2);
        when(fdcContributionsRepository.callGetFdcCalculationDelay()).thenReturn("5");
        FdcContributionsGlobalUpdateResponse response = fdcContributionsService.fdcContributionGlobalUpdate();
        assertNotNull(response);
        assertEquals(expected1+expected2, response.getNumberOfUpdates());
    }

    @Test
    void testAtomicUpdateSuccess(){
        // setup
        CreateFdcFileRequest request = createFdcFileRequest();
        ContributionFilesEntity mappedEntity = createContributionsFileEntity();

        when(debtCollectionRepository.saveContributionFilesEntity(mappedEntity)).thenReturn(true);
        when(fdcContributionsRepository.findByIdIn(request.getFdcIds())).thenReturn(fdcContributionsEntityList);
        when(fdcContributionsRepository.saveAll(fdcContributionsEntityList)).thenReturn(fdcContributionsEntityList);

        when(contributionFileMapper.toContributionFileEntity(request)).thenReturn(mappedEntity);
        // run
        assertTrue(fdcContributionsService.createContributionFileAndUpdateFdcStatus(request));
        // test
        verify(fdcContributionsRepository,times(1)).findByIdIn(any());
        verify(fdcContributionsRepository,times(1)).saveAll(any());
        verify(fdcContributionsRepository,times(1)).findByIdIn(any());
    }

    @Test
    void testNoAtomicUpdateRequest(){
            ValidationException e = assertThrows(ValidationException.class,() -> fdcContributionsService.createContributionFileAndUpdateFdcStatus(null));
            assertEquals("fdcRequest object is null", e.getMessage());
    }

    @Test
    void testNullAtomicUpdateNoIds(){
        CreateFdcFileRequest request = createFdcFileRequest();
        request.setFdcIds(null);
        ValidationException e = assertThrows(ValidationException.class,() -> fdcContributionsService.createContributionFileAndUpdateFdcStatus(request));
        assertEquals("FdcIds is empty/null.", e.getMessage());
    }

    @Test
    void testEmptyAtomicUpdateNoIds(){
        CreateFdcFileRequest request = createFdcFileRequest();
        request.setFdcIds(Set.of());
        ValidationException e = assertThrows(ValidationException.class,() -> fdcContributionsService.createContributionFileAndUpdateFdcStatus(request));
        assertEquals("FdcIds is empty/null.", e.getMessage());
    }

    @Test
    void testDrcUpdateSuccessNoErrorText(){
        int id = 123;
        int repId = 456;
        int fileId = 10000;

        FdcContributionsEntity concorEntity = createFdcContributionsEntity(id, repId, fileId);

        when(fdcContributionsRepository.findById(id)).thenReturn(Optional.of(concorEntity));
        when(debtCollectionService.updateContributionFileReceivedCount(fileId)).thenReturn(true);
        // do
        fdcContributionsService.logFdcProcessed(createLogFdcProcessedRequest(id, ""));
        // verify
        verify(fdcContributionsRepository).findById(id);
        verify(debtCollectionService).updateContributionFileReceivedCount(fileId);
        verify(contributionFileErrorsRepository, times(0)).save(any());
    }

    @Test
    void testDrcUpdateSuccessHasErrorText(){
        int id = 123;
        int repId = 456;
        int fileId = 10000;
        String errorText = "Error Text";
        LocalDateTime currDate = LocalDateTime.now();

        FdcContributionsEntity fdcEntity = createFdcContributionsEntity(id, repId, fileId);

        when(fdcContributionsRepository.findById(id)).thenReturn(Optional.of(fdcEntity));

        when(debtCollectionService.saveError(fileErrorCaptor.capture())).thenReturn(true);
        when(debtCollectionService.updateContributionFileReceivedCount(fileId)).thenReturn(true);
        // do
        fdcContributionsService.logFdcProcessed(createLogFdcProcessedRequest(id, errorText));
        // verify
        verify(fdcContributionsRepository).findById(id);
        verify(debtCollectionService).updateContributionFileReceivedCount(fileId);
        ContributionFileErrorsEntity errorEntity = fileErrorCaptor.getValue();

        assertEquals(errorText,errorEntity.getErrorText());
        assertEquals(id, errorEntity.getFdcContributionId());
        assertEquals(id,errorEntity.getContributionId());
        assertEquals(fileId,errorEntity.getContributionFileId());
        assertNull(errorEntity.getConcorContributionId());
        assertEquals(repId,errorEntity.getRepId());
        assertEquals(currDate.getDayOfMonth(), errorEntity.getDateCreated().getDayOfMonth());
        assertEquals(currDate.getMonth(), errorEntity.getDateCreated().getMonth());
        assertEquals(currDate.getYear(), errorEntity.getDateCreated().getYear());
    }


    @Test
    void testDrcUpdateNoFdcFound(){
        int id = 123;
        String errorText = "Error Text";

        when(fdcContributionsRepository.findById(id)).thenReturn(Optional.empty());
        // do
        fdcContributionsService.logFdcProcessed(createLogFdcProcessedRequest(id, errorText));
        // verify
        verify(fdcContributionsRepository).findById(id);
        verify(contributionFilesRepository,times(0)).findById(any());
        verify(contributionFilesRepository,times(0)).save(any());
        verify(contributionFileErrorsRepository, times(0)).save(any());
    }

    @Test
    void testDrcUpdateNoFileFound(){
        int id = 123;
        int repId = 456;
        int fileId = 10000;
        String errorText = "Error Text";
        FdcContributionsEntity fdcEntity = createFdcContributionsEntity(id, repId, fileId);
        when(fdcContributionsRepository.findById(id)).thenReturn(Optional.of(fdcEntity));
        when(debtCollectionService.updateContributionFileReceivedCount(fileId)).thenReturn(false);
        // do
        boolean result = fdcContributionsService.logFdcProcessed(createLogFdcProcessedRequest(id, errorText));
        assertFalse(result);
        // verify
        verify(fdcContributionsRepository).findById(id);
        verify(debtCollectionService,times(1)).updateContributionFileReceivedCount(any());
        // verify no error is saved, as file is not found.
        verify(debtCollectionService,times(0)).saveError(any());
    }

    private LogFdcProcessedRequest createLogFdcProcessedRequest(int id, String errorText){
        return LogFdcProcessedRequest.builder()
                .fdcId(id)
                .errorText(errorText)
                .build();
    }

    private static FdcContributionsEntity createFdcContributionsEntity(int id, int repId, int fileId){
        RepOrderEntity repOrderEntity = RepOrderEntity.builder()
                .id(repId)
                .build();

        return FdcContributionsEntity.builder()
                .id(id)
                .repOrderEntity(repOrderEntity)
                .contFileId(fileId)
                .build();
    }


    private CreateFdcFileRequest createFdcFileRequest(){
        return CreateFdcFileRequest.builder()
                .xmlFileName("filename.xml")
                .xmlContent("<xml></xml>")
                .ackXmlContent("<ackXml></ackXml>")
                .fdcIds(Set.of(1))
                .build();
    }

    private ContributionFilesEntity createContributionsFileEntity() {
        LocalDate date = LocalDate.now();
        return ContributionFilesEntity.builder()
                .fileId(1)
                .fileName("filename.xml")
                .recordsSent(1)
                .recordsReceived(1)
                .dateCreated(date)
                .userCreated("DCES")
                .dateSent(date)
                .userModified("DCES")
                .xmlContent("<xml>test</xml>")
                .dateModified(date)
                .dateModified(date)
                .ackXmlContent("<xml>ackXML<xml>")
                .build();
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
