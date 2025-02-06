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
    void givenDateRange_whenGetContributionFiles_thenReturnXmlContents() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        when(debtCollectionRepository.getContributionFiles(LocalDate.now().format(dateTimeFormatter),LocalDate.now().format(dateTimeFormatter)))
                .thenReturn(List.of(contributionFilesEntity.getXmlContent()));
        List<String> xmlXontents = debtCollectionService.getContributionFiles(LocalDate.now(),LocalDate.now());
        assertThat(xmlXontents).isNotNull();
        assertThat(xmlXontents.isEmpty()).isFalse();
    }


    @Test
    void givenDateRange_whenGetFdcFiles_thenReturnXmlContents() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        when(debtCollectionRepository.getFdcFiles(LocalDate.now().format(dateTimeFormatter),LocalDate.now().format(dateTimeFormatter)))
                .thenReturn(List.of(contributionFilesEntity.getXmlContent()));
        List<String> xmlXontents = debtCollectionService.getFdcFiles(LocalDate.now(),LocalDate.now());
        assertThat(xmlXontents).isNotNull();
        assertThat(xmlXontents.isEmpty()).isFalse();
    }

    @Test
    void givenDate_whenConvertToCorrectDateFormat_thenReturnFormattedString() {

        String formattedDate = debtCollectionService.convertToCorrectDateFormat( LocalDate.of(2020,9,1));
        assertThat(formattedDate).isEqualTo("01/09/2020");
    }
    @Test
    void givenDate_whenConvertToCorrectDateFormat_thenReturnCorrectFormattedString() {
        String formattedDate = debtCollectionService.convertToCorrectDateFormat( LocalDate.of(2020,9,1));
        Assertions.assertNotEquals("01-09-2020",formattedDate);
    }

    @Test
    void givenFileData_whenSaveError_thenSaveErrorEntity(){
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
        assertThat(entityToSave.getConcorContributionId()).isEqualTo(concorId);
        assertThat(entityToSave.getContributionId()).isEqualTo(concorId);
        assertThat(entityToSave.getFdcContributionId()).isNull();
        assertThat(entityToSave.getRepId()).isEqualTo(repId);
        assertThat(entityToSave.getContributionFileId()).isEqualTo(fileId);
        assertThat(entityToSave.getErrorText()).isEqualTo(errorText);
        assertThat(entityToSave.getDateCreated().getDayOfMonth()).isEqualTo(currDate.getDayOfMonth());
        assertThat(entityToSave.getDateCreated().getMonth()).isEqualTo(currDate.getMonth());
        assertThat(entityToSave.getDateCreated().getYear()).isEqualTo(currDate.getYear());


    }

    @Test
    void givenValidFileId_whenUpdateContributionFileReceivedCount_thenReturnTrueAndIncrementRecordsReceived() {
        final int fileId = 444;
        when(contributionFilesRepository.incrementRecordsReceived(fileId, "DCES")).thenReturn(1);
        assertThat(debtCollectionService.updateContributionFileReceivedCount(fileId)).isTrue();
        verify(contributionFilesRepository).incrementRecordsReceived(fileId, "DCES");
    }

    @Test
    void givenInvalidFileId_whenUpdateContributionFileReceivedCount_thenThrowException() {
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
