package gov.uk.courtdata.dces.service;

import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.testutils.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDate;
import java.util.List;
import java.time.format.DateTimeFormatter;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
@WebMvcTest(DebtCollectionService.class)
class DebtCollectionServiceTest {

    @MockBean
    private DebtCollectionRepository debtCollectionRepository;

    @Autowired
    private DebtCollectionService debtCollectionService;
    private static final String USER_CREATED = "MLA";

    private ContributionFilesEntity contributionFilesEntity;
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
}