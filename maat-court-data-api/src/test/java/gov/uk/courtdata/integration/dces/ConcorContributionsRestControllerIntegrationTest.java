package gov.uk.courtdata.integration.dces;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dces.service.DebtCollectionRepository;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static gov.uk.courtdata.enums.ConcorContributionStatus.SENT;
import static gov.uk.courtdata.enums.ConcorContributionStatus.ACTIVE;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConcorContributionsRestControllerIntegrationTest extends MockMvcIntegrationTest {
    private static final String ATOMIC_UPDATE_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/create-contribution-file";

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/concor-contribution-files?status=";
    private static final String DRC_UPDATE_URL = "/api/internal/v1/debt-collection-enforcement/log-contribution-response";

    @SpyBean
    DebtCollectionRepository debtCollectionRepositorySpy;

    private static int savedEntityId3;
    private static int savedEntityId4;

    private static final String FILE_ONE_NAME = "FileOne";
    private static int file1Id;
    private static final String FILE_TWO_NAME = "FileTwo";
    private static int file2Id;

    @BeforeEach
    public void setUp() {
        file1Id = repos.contributionFiles.save(TestEntityDataBuilder.getContributionFilesEntity(FILE_ONE_NAME, 10, 0, false)).getFileId();
        file2Id = repos.contributionFiles.save(TestEntityDataBuilder.getContributionFilesEntity(FILE_TWO_NAME, 10, 0, true)).getFileId();

        saveConcorEntity(null, ACTIVE, file1Id, "<xml>1 content dummy</xml>");
        saveConcorEntity(null, SENT, file1Id, "<xml>2 content dummy</xml>");
        savedEntityId3 = saveConcorEntity(null, ACTIVE, null, "<xml>3 content dummy</xml>");
        savedEntityId4 = saveConcorEntity(null, SENT, file2Id, "<xml>4 content dummy</xml>");
    }

    private int saveConcorEntity(Integer repId, ConcorContributionStatus status, Integer fileId, String currentXml){
        ConcorContributionsEntity concorEntity = TestEntityDataBuilder.getConcorContributionsEntity(repId, status, fileId, currentXml);
        return repos.concorContributions.save(concorEntity).getId();
    }

    @Test
    void givenAACTIVEStatus_whenGetIsInvoked_theDataLoadedResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"ACTIVE")
                        .queryParam("numberOfRecords", String.valueOf(3))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Order(2)
    void givenAREPLACEDStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"REPLACED")
                        .queryParam("numberOfRecords", String.valueOf(3))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(3)
    void givenAnInvalidStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"XXX")
                        .queryParam("numberOfRecords", String.valueOf(3))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("The provided value 'XXX' is the incorrect type for the 'status' parameter."));
    }

    @Test
    @Order(4)
    void givenZeroNumberOfRecords_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"REPLACED")
                        .queryParam("numberOfRecords", String.valueOf(0))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void givenAListOfIds_whenAtomicUpdate_theStatusIsUpdated() throws Exception {
        // removed the xml values as it cannot be tested due to xmlType. The double refuses to handle XMLTYPE(?) as that is oracle specific.
        int expectedFileListSize = repos.contributionFiles.findAll().size() + 1;
        String expectedFilename = "TestFilename.xml";
        int expectedRecordsSent = 2;
        String s = """
                                {
                                    "recordsSent": %s,
                                    "concorContributionIds": [%s],
                                    "xmlFileName" : "%s"
                                }""";
        s = s.formatted( expectedRecordsSent, savedEntityId3, expectedFilename);
        mockMvc.perform(MockMvcRequestBuilders.post(ATOMIC_UPDATE_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // verify the content of the saved entity.
        List<ContributionFilesEntity> savedFileEntities = repos.contributionFiles.findAll();
        assertEquals(expectedFileListSize, savedFileEntities.size());
        ContributionFilesEntity savedFileEntity = savedFileEntities.get(expectedFileListSize-1); // get last element
        assertNotNull(savedFileEntity);
        assertNotNull(savedFileEntity.getFileId());
        assertEquals(expectedRecordsSent, savedFileEntity.getRecordsSent());
        assertNull(savedFileEntity.getXmlContent());
        assertNull(savedFileEntity.getAckXmlContent());
        assertEquals(expectedFilename, savedFileEntity.getFileName());
        // assert the file id has been set on the contribution
        Optional<ConcorContributionsEntity> updatedConcor = repos.concorContributions.findById(
            savedEntityId3);
        assertTrue(updatedConcor.isPresent());
        ConcorContributionsEntity fdcContributionsEntity = updatedConcor.get();
        assertEquals(savedFileEntity.getFileId(), fdcContributionsEntity.getContribFileId());
        assertEquals(ConcorContributionStatus.SENT, fdcContributionsEntity.getStatus());
    }

    @Test
    void testLogDrcProcessedNoErrors() throws Exception {
        String s = TestModelDataBuilder.getConcorDrcUpdateJson(savedEntityId4,"");

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check we've updated the received count.
        Optional<ContributionFilesEntity> fileEntity = repos.contributionFiles.findById(file2Id);
        assertTrue(fileEntity.isPresent());
        assertEquals(1, fileEntity.get().getRecordsReceived());

        // check no values in errors
        assertEquals(0, repos.contributionFileErrors.count());
    }

    @Test
    void testLogDrcProcessedWithErrors() throws Exception {
        String errorText = "ErrorText";
        String s = TestModelDataBuilder.getConcorDrcUpdateJson(savedEntityId4,errorText);
        LocalDateTime dateTimeCheck = LocalDateTime.now();

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check we've updated the received count.
        Optional<ContributionFilesEntity> fileEntity = repos.contributionFiles.findById(file2Id);
        assertTrue(fileEntity.isPresent());
        assertEquals(0, fileEntity.get().getRecordsReceived());

        // check no values in errors
        assertEquals(1, repos.contributionFileErrors.count());
        ContributionFileErrorsEntity errorEntity = repos.contributionFileErrors.findAll().get(0);
        assertEquals(savedEntityId4, errorEntity.getConcorContributionId());
        assertEquals(savedEntityId4, errorEntity.getContributionId());
        assertEquals(errorText, errorEntity.getErrorText());
        assertEquals(file2Id, errorEntity.getContributionFileId());

        assertEquals(dateTimeCheck.getDayOfMonth(), errorEntity.getDateCreated().getDayOfMonth());
        assertEquals(dateTimeCheck.getMonth(), errorEntity.getDateCreated().getMonth());
        assertEquals(dateTimeCheck.getYear(), errorEntity.getDateCreated().getYear());
    }

    @Test
    void testLogDrcProcessedNoFile() throws Exception {
        String s = TestModelDataBuilder.getConcorDrcUpdateJson(savedEntityId3,"");

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check no values in errors
        assertEquals(0, repos.contributionFileErrors.count());
    }
}
