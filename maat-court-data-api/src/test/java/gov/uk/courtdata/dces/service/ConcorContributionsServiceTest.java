package gov.uk.courtdata.dces.service;

import static gov.uk.courtdata.enums.ConcorContributionStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import gov.uk.courtdata.dces.request.CreateContributionFileRequest;
import gov.uk.courtdata.dces.request.LogContributionProcessedRequest;
import gov.uk.courtdata.dces.response.ConcorContributionResponse;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    private ContributionFileErrorsRepository contributionFileErrorsRepository;


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

        final boolean actualResponse = concorService.createContributionAndUpdateConcorStatus(createContributionFileRequest);

        verify(debtCollectionRepository).save(contributionFilesEntityArgumentCaptor.capture());
        verify(concorRepository).saveAll(concorContributionEntityListArgumentCaptor.capture());

        final ContributionFilesEntity actualContributionFileEntity = contributionFilesEntityArgumentCaptor.getValue();
        final List<ConcorContributionsEntity> contributionEntityList =  concorContributionEntityListArgumentCaptor.getValue();

        assertTrue(actualResponse);
        assertNotNull(actualContributionFileEntity);
        assertEquals("<xml>ackDummyContent</xml>", actualContributionFileEntity.getAckXmlContent());
        assertEquals(10, actualContributionFileEntity.getRecordsSent());
        assertEquals(1, actualContributionFileEntity.getId());
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

        boolean actualResponse = concorService.createContributionAndUpdateConcorStatus(createContributionFileRequest);

        verify(debtCollectionRepository).save(contributionFilesEntityArgumentCaptor.capture());
        assertFalse(actualResponse);
        assertEquals(1, contributionFilesEntityArgumentCaptor.getValue().getId());
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
    void testDrcUpdateSuccessNoErrorText(){
        int id = 123;
        int repId = 456;
        int fileId = 10000;

        ContributionFilesEntity fileEntity = getContributionFilesEntity(fileId);
        ContributionFilesEntity originalFileEntity = getContributionFilesEntity(fileId);
        ConcorContributionsEntity concorEntity = createConcorContributionEntity(id, repId, fileId);

        when(concorRepository.findById(id)).thenReturn(Optional.of(concorEntity));
        when(contributionFileRepository.findById(fileId)).thenReturn(Optional.of(fileEntity));
        // do
        concorService.logContributionProcessed(createLogContributionProcessedRequest(id, ""));
        // verify
        verify(concorRepository).findById(id);
        verify(contributionFileRepository).findById(fileId);
        verify(contributionFileRepository,times(1)).save(contributionFilesEntityArgumentCaptor.capture());
        ContributionFilesEntity savedEntity = contributionFilesEntityArgumentCaptor.getValue();
        assertTrue(Objects.nonNull(savedEntity));
        // we should have incremented the received.
        assertEquals(originalFileEntity.getRecordsReceived()+1, savedEntity.getRecordsReceived());
        // only changes should be the received count. So increment and ensure equal.
        imitateUpdateContributionFileUpdate(originalFileEntity);
        assertEquals(originalFileEntity, savedEntity);
        verify(contributionFileErrorsRepository, times(0)).save(any());
    }

    @Test
    void testDrcUpdateSuccessHasErrorText(){
        int id = 123;
        int repId = 456;
        int fileId = 10000;
        String errorText = "Error Text";

        ContributionFilesEntity fileEntity = getContributionFilesEntity(fileId);
        ContributionFilesEntity originalFileEntity = getContributionFilesEntity(fileId);
        ConcorContributionsEntity concorEntity = createConcorContributionEntity(id, repId, fileId);

        when(concorRepository.findById(id)).thenReturn(Optional.of(concorEntity));
        when(contributionFileRepository.findById(fileId)).thenReturn(Optional.of(fileEntity));
        // do
        concorService.logContributionProcessed(createLogContributionProcessedRequest(id, errorText));
        // verify
        verify(concorRepository).findById(id);
        verify(contributionFileRepository).findById(fileId);
        verify(contributionFileRepository,times(1)).save(contributionFilesEntityArgumentCaptor.capture());
        ContributionFilesEntity savedEntity = contributionFilesEntityArgumentCaptor.getValue();
        assertTrue(Objects.nonNull(savedEntity));
        // we should have incremented the received.
        assertEquals(originalFileEntity.getRecordsReceived()+1, savedEntity.getRecordsReceived());
        // only changes should be the received count. So increment and ensure equal.
        imitateUpdateContributionFileUpdate(originalFileEntity);
        assertEquals(originalFileEntity, savedEntity);
        verify(contributionFileErrorsRepository, times(1)).save(fileErrorCaptor.capture());
        ContributionFileErrorsEntity errorEntity = fileErrorCaptor.getValue();

        assertEquals(errorText,errorEntity.getErrorText());
        assertEquals(id, errorEntity.getConcorContributionId());
        assertEquals(id,errorEntity.getContributionId());
        assertEquals(fileId,errorEntity.getContributionFileId());
        assertNull(errorEntity.getFdcContributionId());
        assertNull(errorEntity.getDateCreated()); // this is auto-populated by jpa.
        assertEquals(repId,errorEntity.getRepId());

    }


    private LogContributionProcessedRequest createLogContributionProcessedRequest(int id, String errorText){
        return LogContributionProcessedRequest.builder()
                .concorId(id)
                .errorText(errorText)
                .build();
    }

    private void imitateUpdateContributionFileUpdate(ContributionFilesEntity fileEntity){
        LocalDate currentDate = LocalDate.now();
        fileEntity.setDateModified(currentDate);
        fileEntity.setDateReceived(currentDate);
        fileEntity.incrementReceivedCount();
        fileEntity.setUserModified("DCES");
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

    private static ConcorContributionsEntity createConcorContributionEntity(int id, int repId, int fileId){
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
        concorContributionFiles= new ArrayList<>();
        ConcorContributionsEntity concorFile = new ConcorContributionsEntity();
        concorFile.setCurrentXml(xmlDoc);
        concorFile.setStatus(ACTIVE);
        concorContributionFiles.add(concorFile);
    }
}
