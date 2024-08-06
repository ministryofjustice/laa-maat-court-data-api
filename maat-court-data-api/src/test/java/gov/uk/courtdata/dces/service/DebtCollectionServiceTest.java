package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.dces.request.LogContributionProcessedRequest;
import gov.uk.courtdata.dces.util.ContributionFileUtil;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.repository.ContributionFileErrorsRepository;
import gov.uk.courtdata.repository.ContributionFilesRepository;
import gov.uk.courtdata.testutils.FileUtils;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtCollectionServiceTest {

    @Mock
    private DebtCollectionRepository debtCollectionRepository;

    @Mock
    private ContributionFilesRepository contributionFilesRepository;
    @Mock
    private ContributionFileErrorsRepository contributionFileErrorsRepository;

    @InjectMocks
    private DebtCollectionService debtCollectionService;
    private static final String USER_CREATED = "MLA";

    private ContributionFilesEntity contributionFilesEntity;

    @Captor
    private ArgumentCaptor<ContributionFileErrorsEntity> errorEntityCaptor;
    @Captor
    private ArgumentCaptor<ContributionFilesEntity> contributionFilesEntityArgumentCaptor;

   @BeforeEach
    void setUp() {
       String xmlDoc = FileUtils.readResourceToString("eform/request/xmlDoc_default.xml");
       getContributionFile(xmlDoc);

   }

    private void getContributionFile(String xmlDoc) {
        contributionFilesEntity =  ContributionFilesEntity.builder()
                .xmlContent(xmlDoc)
                .userCreated(USER_CREATED)
                .dateReceived(LocalDate.now())
                .dateSent(LocalDate.now())
                .build();
    }


    @Test
    void givenDateRange_WhenServiceInvoke_ContributionFile_ThenGetXMLContents() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        when(debtCollectionRepository.getContributionFiles(LocalDate.now().format(dateTimeFormatter),LocalDate.now().format(dateTimeFormatter)))
                .thenReturn(List.of(contributionFilesEntity.getXmlContent()));
        List<String> xmlXontents = debtCollectionService.getContributionFiles(LocalDate.now(),LocalDate.now());
        assertNotNull(xmlXontents);
        Assertions.assertTrue(xmlXontents.size()>0);
    }


    @Test
    void givenDateRange_WhenServiceInvoke_FdcFile_ThenGetXMLContents() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        when(debtCollectionRepository.getFdcFiles(LocalDate.now().format(dateTimeFormatter),LocalDate.now().format(dateTimeFormatter)))
                .thenReturn(List.of(contributionFilesEntity.getXmlContent()));
        List<String> xmlXontents = debtCollectionService.getFdcFiles(LocalDate.now(),LocalDate.now());
        assertNotNull(xmlXontents);
        Assertions.assertTrue(xmlXontents.size()>0);
    }

    @Test
    void givenDate_WhenMethodInvoke_ThenGetFormattedStringDate() {

        String formattedDate = debtCollectionService.convertToCorrectDateFormat( LocalDate.of(2020,9,1));
        Assertions.assertEquals("01/09/2020",formattedDate);
    }
    @Test
    void givenDate_WhenMethodInvoke_ThenGet_WrongFormattedStringDate() {
        String formattedDate = debtCollectionService.convertToCorrectDateFormat( LocalDate.of(2020,9,1));
        Assertions.assertNotEquals("01-09-2020",formattedDate);
    }

    @Test
    void givenFileData_ShouldSaveError(){
       int concorId = 444;
       int fileId = 999;
       int repId = 111;
       String errorText = "ErrorText";
       LocalDate currDate = LocalDate.now();

       when(contributionFileErrorsRepository.save(errorEntityCaptor.capture())).thenReturn(null);

       ConcorContributionsEntity concorEntity = ConcorContributionsEntity.builder().id(concorId).contribFileId(fileId).repId(repId).build();
       LogContributionProcessedRequest request = LogContributionProcessedRequest.builder().concorId(concorId).errorText(errorText).build();
       debtCollectionService.saveError(ContributionFileUtil.buildContributionFileError(request,concorEntity));

        ContributionFileErrorsEntity entityToSave = errorEntityCaptor.getValue();
        assertEquals(concorId, entityToSave.getConcorContributionId());
        assertEquals(concorId, entityToSave.getContributionId());
        assertNull(entityToSave.getFdcContributionId());
        assertEquals(repId, entityToSave.getRepId());
        assertEquals(fileId, entityToSave.getContributionFileId());
        assertEquals(errorText, entityToSave.getErrorText());
        assertEquals(currDate.getDayOfMonth(), entityToSave.getDateCreated().getDayOfMonth());
        assertEquals(currDate.getMonth(), entityToSave.getDateCreated().getMonth());
        assertEquals(currDate.getYear(), entityToSave.getDateCreated().getYear());


    }

    @Test
    void testUpdateContributionFileReceivedCountSuccess() {
        final int fileId = 444;
        when(contributionFilesRepository.incrementRecordsReceived(fileId, "DCES")).thenReturn(1);
        assertThat(debtCollectionService.updateContributionFileReceivedCount(fileId)).isTrue();
        verify(contributionFilesRepository).incrementRecordsReceived(fileId, "DCES");
    }

    @Test
    void testUpdateContributionFileReceivedCountNoFile() {
        final int fileId = 444;
        when(contributionFilesRepository.incrementRecordsReceived(fileId, "DCES")).thenReturn(0);
        assertThatThrownBy(() -> debtCollectionService
                .updateContributionFileReceivedCount(fileId))
                .isInstanceOf(MAATCourtDataException.class)
                .hasMessageContaining("0 row(s) updated");
    }

    private ContributionFilesEntity getContributionFilesEntity(int id) {
        return new ContributionFilesEntity(
                id,
                "dummyFile.txt",
                10,
                0,
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
