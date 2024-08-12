package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.mapper.ConcorContributionMapper;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.LogContributionProcessedRequest;
import gov.uk.courtdata.dces.request.UpdateConcorContributionStatusRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponse;
import gov.uk.courtdata.dces.response.ConcorContributionResponseDTO;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import gov.uk.courtdata.testutils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static gov.uk.courtdata.enums.ConcorContributionStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConcorContributionsServiceTest {
    private List<ConcorContributionsEntity> concorContributionFiles;

    @InjectMocks
    private ConcorContributionsService concorService;

    @Mock
    private ConcorContributionsRepository concorRepository;

    @Mock
    private ContributionFilesRepository contributionFileRepository;

    @Mock
    private ContributionFileMapper contributionFileMapper;

    @Mock
    private DebtCollectionRepository debtCollectionRepository;

    @Mock
    private DebtCollectionService debtCollectionService;

    @Mock
    private ContributionFileErrorsRepository contributionFileErrorsRepository;

    @Mock
    private ConcorContributionMapper concorContributionMapper;

    @Captor
    private ArgumentCaptor<ContributionFilesEntity> contributionFilesEntityArgumentCaptor;
    @Captor
    private ArgumentCaptor<List<ConcorContributionsEntity>> concorContributionEntityListArgumentCaptor;
    @Captor
    private ArgumentCaptor<ConcorContributionStatus> statusCaptor;
    @Captor
    private ArgumentCaptor<ContributionFileErrorsEntity> fileErrorCaptor;

    @BeforeEach
    void setUp() {
        String xmlDoc = getXmlDocContent();
        getContributionFile(xmlDoc);
    }

    @Test
    void testGetContributionFilesWhenConcorFileStatusIsActive() {
        when(concorRepository.findByStatus(statusCaptor.capture())).thenReturn(concorContributionFiles);

        List<ConcorContributionResponse> xmlContents = concorService.getConcorContributionFiles(ACTIVE);

        assertNotNull(xmlContents);
        Assertions.assertFalse(xmlContents.isEmpty());
        final ConcorContributionStatus capturedStatus = statusCaptor.getValue();
        assertEquals(ACTIVE, capturedStatus);
    }

    @Test
    void testWhenContributionRequestIsNullAndThrowException() {
        Assertions.assertThrows(ValidationException.class,
                () -> concorService.createContributionAndUpdateConcorStatus(null));
        verify(contributionFileRepository, never()).save(any());
        verify(concorRepository, never()).findByStatus(any(ConcorContributionStatus.class));
        verify(concorRepository, never()).saveAll(anyList());
    }

    @Test
    void testCreateContributionFileAndUpdateConcorContributionStatus() {
        final CreateContributionFileRequest createContributionFileRequest
                = CreateContributionFileRequest.builder()
                .concorContributionIds(Set.of(1))
                .xmlContent(getXmlDocContent())
                .build();
        final ContributionFilesEntity dummyEntity = getContributionFilesEntity(1);

        when(concorRepository.findByIdIn(any())).thenReturn(concorContributionFiles);
        when(contributionFileMapper.toContributionFileEntity(createContributionFileRequest)).thenReturn(dummyEntity);

        final var actualResponse = concorService.createContributionAndUpdateConcorStatus(createContributionFileRequest);

        verify(debtCollectionRepository).saveContributionFilesEntity(contributionFilesEntityArgumentCaptor.capture());
        verify(concorRepository).saveAll(concorContributionEntityListArgumentCaptor.capture());

        final ContributionFilesEntity actualContributionFileEntity = contributionFilesEntityArgumentCaptor.getValue();
        final List<ConcorContributionsEntity> contributionEntityList =  concorContributionEntityListArgumentCaptor.getValue();

        assertNotNull(actualResponse);
        assertNotNull(actualContributionFileEntity);
        assertEquals("<xml>ackDummyContent</xml>", actualContributionFileEntity.getAckXmlContent());
        assertEquals(10, actualContributionFileEntity.getRecordsSent());
        assertEquals(1, actualContributionFileEntity.getFileId());
        assertNotNull(contributionEntityList);
    }

    @Test
    void testWhenContributionFileWithActiveStatusNotFound() {
        final CreateContributionFileRequest createContributionFileRequest = CreateContributionFileRequest.builder()
                .concorContributionIds(Set.of(1))
                .xmlContent(getXmlDocContent()).build();
        ContributionFilesEntity dummyEntity = getContributionFilesEntity(1);

        when(concorRepository.findByIdIn(any())).thenReturn(new ArrayList<>());
        when(contributionFileMapper.toContributionFileEntity(createContributionFileRequest)).thenReturn(dummyEntity);

        assertThrows(NoSuchElementException.class, () ->
                concorService.createContributionAndUpdateConcorStatus(createContributionFileRequest));

        verify(debtCollectionRepository).saveContributionFilesEntity(contributionFilesEntityArgumentCaptor.capture());
        assertEquals(1, contributionFilesEntityArgumentCaptor.getValue().getFileId());
        verify(concorRepository, never()).saveAll(anyList());
    }

    @Test
    void createContributionAndUpdateConcorStatusWhenCallFailed() {
        final CreateContributionFileRequest mockDto = mock(CreateContributionFileRequest.class);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> concorService.createContributionAndUpdateConcorStatus(mockDto));

        assertEquals("ContributionIds are empty/null.", exception.getMessage());
        verify(contributionFileRepository, never()).save(any());
        verify(concorRepository, never()).findByStatus(any(ConcorContributionStatus.class));
        verify(concorRepository, never()).saveAll(anyList());
    }

    @Test
    void testDrcUpdateSuccessNoErrorText() {
        int id = 123;
        int repId = 456;
        int fileId = 10000;

        ConcorContributionsEntity concorEntity = createConcorContributionEntity(id, repId, fileId);

        when(concorRepository.findById(id)).thenReturn(Optional.of(concorEntity));
        when(debtCollectionService.updateContributionFileReceivedCount(fileId)).thenReturn(true);
        // do
        concorService.logContributionProcessed(createLogContributionProcessedRequest(id, ""));
        // verify
        verify(concorRepository).findById(id);
        verify(debtCollectionService).updateContributionFileReceivedCount(fileId);
        verify(contributionFileErrorsRepository, never()).save(any());
    }

    @Test
    void testDrcUpdateSuccessHasErrorText() {
        int id = 123;
        int repId = 456;
        int fileId = 10000;
        String errorText = "Error Text";
        LocalDateTime currDate = LocalDateTime.now();

        ConcorContributionsEntity concorEntity = createConcorContributionEntity(id, repId, fileId);

        when(concorRepository.findById(id)).thenReturn(Optional.of(concorEntity));

        doNothing().when(debtCollectionService).saveError(fileErrorCaptor.capture());
        // do
        concorService.logContributionProcessed(createLogContributionProcessedRequest(id, errorText));
        // verify
        verify(concorRepository).findById(id);
        verify(debtCollectionService, never()).updateContributionFileReceivedCount(fileId);
        ContributionFileErrorsEntity errorEntity = fileErrorCaptor.getValue();

        assertEquals(errorText,errorEntity.getErrorText());
        assertEquals(id, errorEntity.getConcorContributionId());
        assertEquals(id,errorEntity.getContributionId());
        assertEquals(fileId,errorEntity.getContributionFileId());
        assertNull(errorEntity.getFdcContributionId());
        assertEquals(repId,errorEntity.getRepId());
        assertEquals(currDate.getDayOfMonth(), errorEntity.getDateCreated().getDayOfMonth());
        assertEquals(currDate.getMonth(), errorEntity.getDateCreated().getMonth());
        assertEquals(currDate.getYear(), errorEntity.getDateCreated().getYear());
    }

    @Test
    void testDrcUpdateNoConcorFound() {
        int id = 123;
        String errorText = "Error Text";

        when(concorRepository.findById(id)).thenReturn(Optional.empty());
        // do
        assertThrows(RequestedObjectNotFoundException.class, () ->
            concorService.logContributionProcessed(createLogContributionProcessedRequest(id, errorText)));
        // verify
        verify(concorRepository).findById(id);
        verify(contributionFileRepository, never()).findById(any());
        verify(contributionFileRepository, never()).save(any());
        verify(contributionFileErrorsRepository, never()).save(any());
    }

    @Test
    void testDrcUpdateNoFileFound() {
        int id = 123;
        int repId = 456;
        int fileId = 10000;

        ConcorContributionsEntity concorEntity = createConcorContributionEntity(id, repId, fileId);
        when(concorRepository.findById(id)).thenReturn(Optional.of(concorEntity));
        when(debtCollectionService.updateContributionFileReceivedCount(fileId)).thenReturn(false);
        // do
        assertThrows(NoSuchElementException.class, () ->
                concorService.logContributionProcessed(createLogContributionProcessedRequest(id, "")));
        // verify
        verify(concorRepository).findById(id);
        verify(debtCollectionService).updateContributionFileReceivedCount(any());
        // verify no error is saved, as file is not found.
        verify(debtCollectionService, never()).saveError(any());
    }

    @Test
    void testUpdateConcorContributionsStatus() {
        when(concorRepository.findIdsForUpdate(any())).thenReturn(List.of(1111, 2222));
        when(concorRepository.updateStatusAndResetContribFileForIds(any(), anyString(), any())).thenReturn(2);

        List<Integer> response = concorService.updateConcorContributionStatusAndResetContribFile(UpdateConcorContributionStatusRequest.builder().recordCount(2)
                .status(ConcorContributionStatus.SENT).build());

        verify(concorRepository).findIdsForUpdate(any());
        verify(concorRepository).updateStatusAndResetContribFileForIds(any(), anyString(), any());
        assertEquals(2, response.size());
    }

    @Test
    void testUpdateConcorContributionsStatusWhenNotFound() {
        when(concorRepository.findIdsForUpdate(any())).thenReturn(List.of());

        List<Integer> response = concorService.updateConcorContributionStatusAndResetContribFile(UpdateConcorContributionStatusRequest.builder().recordCount(1)
                .status(ConcorContributionStatus.SENT).build());

        verify(concorRepository).findIdsForUpdate(any());
        verify(concorRepository,never()).updateStatusAndResetContribFileForIds(any(), anyString(), any());
        assertEquals(0, response.size());
    }

    @Test
    void testGetConcorContributionWhenFound() {
        Integer concorContributionId = 1;
        ConcorContributionsEntity concorContributionsEntity = new ConcorContributionsEntity();
        ConcorContributionResponseDTO expectedResponse = ConcorContributionResponseDTO
                .builder()
                .id(concorContributionId)
                .status(ACTIVE)
                .build();

        when(concorRepository.findById(concorContributionId)).thenReturn(Optional.of(concorContributionsEntity));
        when(concorContributionMapper.toConcorContributionResponseDTO(concorContributionsEntity)).thenReturn(expectedResponse);

        ConcorContributionResponseDTO actualResponse = concorService.getConcorContribution(concorContributionId);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetConcorContributionWhenNotFound() {
        Integer concorContributionId = 1;

        when(concorRepository.findById(concorContributionId)).thenReturn(Optional.empty());
        ConcorContributionResponseDTO actualResponse = concorService.getConcorContribution(concorContributionId);

        assertNull(actualResponse);
    }

    private LogContributionProcessedRequest createLogContributionProcessedRequest(int id, String errorText) {
        return LogContributionProcessedRequest.builder()
                .concorId(id)
                .errorText(errorText)
                .build();
    }

    private static ContributionFilesEntity getContributionFilesEntity(int id) {
        return new ContributionFilesEntity(
                id,
                "dummyFile.txt",
                10,
                8,
                LocalDate.of(2023, 10, 10),
                "dummyUser",
                LocalDate.of(2023, 10, 11),
                "anotherDummyUser",
                "<xml>dummyContent</xml>",
                LocalDate.of(2023, 10, 12),
                LocalDate.of(2023, 10, 13),
                "<xml>ackDummyContent</xml>"
        );
    }

    private static ConcorContributionsEntity createConcorContributionEntity(int id, int repId, int fileId) {
        return ConcorContributionsEntity.builder()
                .id(id)
                .repId(repId)
                .contribFileId(fileId)
                .build();
    }

    @NotNull
    private static String getXmlDocContent() {
        return FileUtils.readResourceToString("eform/request/xmlDoc_default.xml");
    }

    private void getContributionFile(String xmlDoc) {
        concorContributionFiles = new ArrayList<>();
        ConcorContributionsEntity concorFile = new ConcorContributionsEntity();
        concorFile.setCurrentXml(xmlDoc);
        concorFile.setStatus(ACTIVE);
        concorContributionFiles.add(concorFile);
    }
}
