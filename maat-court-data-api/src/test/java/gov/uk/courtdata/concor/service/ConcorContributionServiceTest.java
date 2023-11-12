package gov.uk.courtdata.concor.service;

import static org.mockito.Mockito.*;

import gov.uk.courtdata.dces.enums.ConcorContributionStatus;
import gov.uk.courtdata.dces.request.ConcorContributionRequest;
import gov.uk.courtdata.dces.mapper.ContributionFileMapper;
import gov.uk.courtdata.dces.service.ConcorContributionsService;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import gov.uk.courtdata.testutils.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

@ExtendWith(MockitoExtension.class)
class ConcorContributionServiceTest {

    @InjectMocks
    private ConcorContributionsService concorService;

    @Mock
    private ConcorContributionsRepository concorRepository;

    @Mock
    private ContributionFilesRepository contributionFileRepository;

    @Mock
    private ContributionFileMapper contributionFileMapper;

    private List<ConcorContributionsEntity> concorContributionFiles;

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

    @NotNull
    private static String getXmlDocContent() {
        return FileUtils.readResourceToString("eform/request/xmlDoc_default.xml");
    }

    private void getContributionFile(String xmlDoc) {
        concorContributionFiles= new ArrayList<>();
        ConcorContributionsEntity concorFile = new ConcorContributionsEntity();
        concorFile.setCurrentXml(xmlDoc);
        concorFile.setStatus(ConcorContributionStatus.ACTIVE);
        concorContributionFiles.add(concorFile);
    }

    @Test
    void givenConcorFileStatuse_WhenServiceInvoke_getConcorFiles_ThenGetListOfXMLContents() {
        when(concorRepository.findByStatus(ConcorContributionStatus.ACTIVE)).thenReturn(concorContributionFiles);

        List<String> xmlContents = concorService.getConcorFiles(ConcorContributionStatus.ACTIVE);

        Assertions.assertNotNull(xmlContents);
        Assertions.assertFalse(xmlContents.isEmpty());
    }

    @Test
    void givenConcordFileStatus_WhenServiceInvoke_getConcordFiles_ThenGetListOfXMLContents_with_captor() {

        when(concorRepository.findByStatus(statusCaptor.capture())).thenReturn(concorContributionFiles);

        List<String> xmlContents = concorService.getConcorFiles(ConcorContributionStatus.ACTIVE);

        Assertions.assertNotNull(xmlContents);
        Assertions.assertFalse(xmlContents.isEmpty());
        ConcorContributionStatus capturedStatus = statusCaptor.getValue();
        Assertions.assertEquals(ConcorContributionStatus.ACTIVE, capturedStatus);
    }

    @Test
    void givenContributionFileDTO_WhenServiceInvoke_getConcorFiles_ThenUpdateConcorFileReferences() {
        final ConcorContributionRequest concorContributionRequest = new ConcorContributionRequest( 123,getXmlDocContent());
        final ContributionFilesEntity dummyEntity = getContributionFilesEntity();

        when(concorRepository.findByStatus(ConcorContributionStatus.ACTIVE)).thenReturn(concorContributionFiles);
        when(contributionFileMapper.toContributionFileEntity(concorContributionRequest)).thenReturn(dummyEntity);

        concorService.createContributionFileAndUpdateConcorContributionsStatus(concorContributionRequest);

        verify(contributionFileRepository).save(contributionEntityArgumentCaptor.capture());
        verify(concorRepository).saveAll(concorContributionEntityArgumentCaptor.capture());

        ContributionFilesEntity contributionEntityArgumentCaptorValue =  contributionEntityArgumentCaptor.getValue();
        List<ConcorContributionsEntity> contributionEntityList =  concorContributionEntityArgumentCaptor.getValue();
        Assertions.assertNotNull(contributionEntityArgumentCaptorValue);
        Assertions.assertEquals(10, contributionEntityArgumentCaptorValue.getRecordsSent());
        Assertions.assertNotNull(contributionEntityList);
    }

    @Test
    void givenContributionFileDTO_WhenServiceInvoke_getConcorFiles_ThenUpdate() {
        final ConcorContributionRequest concorContributionRequest = new ConcorContributionRequest(123, getXmlDocContent());
        ContributionFilesEntity dummyEntity = getContributionFilesEntity();

        when(concorRepository.findByStatus(ConcorContributionStatus.ACTIVE)).thenReturn(new ArrayList<>());
        when(contributionFileMapper.toContributionFileEntity(concorContributionRequest)).thenReturn(dummyEntity);

        concorService.createContributionFileAndUpdateConcorContributionsStatus(concorContributionRequest);

        verify(contributionFileRepository).save(contributionEntityArgumentCaptor.capture());
        verify(concorRepository, never()).saveAll(anyList());
    }

    @Test
    void givenContributionFileDTO_WhenServiceInvoke_getExceptionWithMapper_ThenOtherRepoMethodsNotCalled() {

        final ConcorContributionRequest mockDto = mock(ConcorContributionRequest.class);
        when(contributionFileMapper.toContributionFileEntity(mockDto)).thenThrow(new MAATCourtDataException("ConcorContributionRequest to ContributionEntity Mapper error."));

        MAATCourtDataException exception = Assertions.assertThrows(MAATCourtDataException.class, () -> concorService.createContributionFileAndUpdateConcorContributionsStatus(mockDto));

        Assertions.assertEquals("ConcorContributionRequest to ContributionEntity Mapper error.", exception.getMessage());
        verify(contributionFileRepository, never()).save(any());
        verify(concorRepository, never()).findByStatus(any(ConcorContributionStatus.class));
        verify(concorRepository, never()).saveAll(anyList());
    }

    @DisplayName("Contribution when exception and everything canceled.")
    @Test
    void givenContributionFileDTO_WhenServiceInvoke_NullDTO_ThenThrowException() {
        Assertions.assertThrows(MAATCourtDataException.class,
                () -> concorService.createContributionFileAndUpdateConcorContributionsStatus(null));
        verify(contributionFileRepository, never()).save(any());
        verify(concorRepository, never()).findByStatus(any(ConcorContributionStatus.class));
        verify(concorRepository, never()).saveAll(anyList());
    }

    @Test
    void given_WhenServiceInvoke_getMaxAvailableId() {
        when(contributionFileRepository.findMaxId()).thenReturn(100);
        final int maxAvailableId = concorService.getMaxAvailableId();
        Assertions.assertEquals(101, maxAvailableId);
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
}
