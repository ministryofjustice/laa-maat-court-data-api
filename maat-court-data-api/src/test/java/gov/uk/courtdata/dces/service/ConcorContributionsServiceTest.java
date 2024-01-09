package gov.uk.courtdata.dces.service;

import static gov.uk.courtdata.enums.ConcorContributionStatus.ACTIVE;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import gov.uk.courtdata.dces.response.ConcorContributionResponse;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.request.ConcorContributionRequest;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
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


    @Captor
    private ArgumentCaptor<ContributionFilesEntity> contributionEntityArgumentCaptor;
    @Captor
    private ArgumentCaptor<List<ConcorContributionsEntity>> concorContributionEntityArgumentCaptor;
    @Captor
    private ArgumentCaptor<ConcorContributionStatus> statusCaptor;

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

        final ConcorContributionRequest concorContributionRequest
                = ConcorContributionRequest.builder().concorContributionIds(Set.of(1)).xmlContent(getXmlDocContent()).build();
        final ContributionFilesEntity dummyEntity = getContributionFilesEntity();

        when(concorRepository.findByIdIn(any())).thenReturn(concorContributionFiles);
        when(contributionFileMapper.toContributionFileEntity(concorContributionRequest)).thenReturn(dummyEntity);

        final boolean actualResponse = concorService.createContributionAndUpdateConcorStatus(concorContributionRequest);

        verify(debtCollectionRepository).save(contributionEntityArgumentCaptor.capture());
        verify(concorRepository).saveAll(concorContributionEntityArgumentCaptor.capture());

        final ContributionFilesEntity actualContributionFileEntity = contributionEntityArgumentCaptor.getValue();
        final List<ConcorContributionsEntity> contributionEntityList =  concorContributionEntityArgumentCaptor.getValue();

        assertTrue(actualResponse);
        assertNotNull(actualContributionFileEntity);
        assertEquals("<xml>ackDummyContent</xml>", actualContributionFileEntity.getAckXmlContent());
        assertEquals(10, actualContributionFileEntity.getRecordsSent());
        assertEquals(1, actualContributionFileEntity.getId());
        assertNotNull(contributionEntityList);
    }

    @Test
    void testWhenContributionFileWithActiveStatusNotFound() {
        final ConcorContributionRequest concorContributionRequest = ConcorContributionRequest.builder()
                .concorContributionIds(Set.of(1))
                .xmlContent(getXmlDocContent()).build();
        ContributionFilesEntity dummyEntity = getContributionFilesEntity();

        when(concorRepository.findByIdIn(any())).thenReturn(new ArrayList<>());
        when(contributionFileMapper.toContributionFileEntity(concorContributionRequest)).thenReturn(dummyEntity);

        boolean actualResponse = concorService.createContributionAndUpdateConcorStatus(concorContributionRequest);

        verify(debtCollectionRepository).save(contributionEntityArgumentCaptor.capture());
        assertFalse(actualResponse);
        assertEquals(1, contributionEntityArgumentCaptor.getValue().getId());
        verify(concorRepository, never()).saveAll(anyList());
    }

    @Test
    void createContributionAndUpdateConcorStatusWhenCallFailed() {

        final ConcorContributionRequest mockDto = mock(ConcorContributionRequest.class);

        ValidationException exception = Assertions.assertThrows(ValidationException.class,
                () -> concorService.createContributionAndUpdateConcorStatus(mockDto));

        assertEquals("ContributionIds are empty/null.", exception.getMessage());
        verify(contributionFileRepository, never()).save(any());
        verify(concorRepository, never()).findByStatus(any(ConcorContributionStatus.class));
        verify(concorRepository, never()).saveAll(anyList());
    }

    @NotNull
    private static ContributionFilesEntity getContributionFilesEntity() {
        return new ContributionFilesEntity(
                1,
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
