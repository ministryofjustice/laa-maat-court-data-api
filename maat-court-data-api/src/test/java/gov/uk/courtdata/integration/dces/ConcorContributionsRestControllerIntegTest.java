package gov.uk.courtdata.integration.dces;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.dces.service.DebtCollectionRepository;
import gov.uk.courtdata.entity.ConcorContributionsEntity;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.enums.ConcorContributionStatus;
import gov.uk.courtdata.repository.ConcorContributionsRepository;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConcorContributionsRestControllerIntegTest extends MockMvcIntegrationTest {

    private static final String ATOMIC_UPDATE_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/create-contribution-file";

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/concor-contribution-files?status=";
    private static final String DRC_UPDATE_URL = "/api/internal/v1/debt-collection-enforcement/log-contribution-processed";

    @Autowired
    private ConcorContributionsRepository concorRepository;

    @SpyBean
    DebtCollectionRepository debtCollectionRepositorySpy;
    @Captor
    private ArgumentCaptor<ContributionFilesEntity> contributionFileEntityCaptor;

    private static int savedEntityId1;
    private static int savedEntityId2;
    private static int savedEntityId3;
    private static int savedEntityId4;

    private static String fileOne = "FileOne";
    private static int file1Id;
    private static String fileTwo = "FileTwo";
    private static int file2Id;

    @BeforeEach
    public void setUp() {
        file1Id = repos.contributionFiles.save(buildFileEntity(fileOne)).getId();
        file2Id = repos.contributionFiles.save(buildFileEntity(fileOne)).getId();


        savedEntityId1 = repos.concorContributions.save(buildConcorEntity(ConcorContributionStatus.ACTIVE, "<xml 1 content dummy",file1Id)).getId();
        savedEntityId2 = repos.concorContributions.save(buildConcorEntity(ConcorContributionStatus.SENT, "<xml 2 content dummy",file1Id)).getId();
        savedEntityId3 = repos.concorContributions.save(buildConcorEntity(ConcorContributionStatus.ACTIVE, "<xml 3 content dummy",null)).getId();
        savedEntityId4 = repos.concorContributions.save(buildConcorEntity(ConcorContributionStatus.SENT, "<xml 4 content dummy",file2Id)).getId();
    }

    private ConcorContributionsEntity buildConcorEntity(ConcorContributionStatus status, String xml, Integer fileId){
        return ConcorContributionsEntity.builder()
                .status(status)
                .currentXml(xml)
                .contribFileId(fileId)
                .build();
    }

    private ContributionFilesEntity buildFileEntity(String fileIdentifier){
        return ContributionFilesEntity.builder()
                .fileName(fileIdentifier)
                .recordsReceived(0)
                .recordsSent(10)
                .xmlContent("<xml>"+fileIdentifier+"</xml>")
                .ackXmlContent("<ackXml>"+fileIdentifier+"</ackXml>")
                .build();
    }

    @Test
    @Order(1)
    void givenAACTIVEStatus_whenGetIsInvoked_theDataLoadedResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"ACTIVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Order(2)
    void givenAREPLACEDStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"REPLACED")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @Order(3)
    void givenAnInvalidStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"XXX")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("The provided value 'XXX' is the incorrect type for the 'status' parameter."));
    }

    @Test
    void givenAListOfIds_whenAtomicUpdate_theStatusIsUpdated() throws Exception {
        // mocking out saveNonXMLs as it cannot be tested due to xmlType.
        doReturn(true).when(debtCollectionRepositorySpy).saveXmlTypes(contributionFileEntityCaptor.capture());

        String expectedXml = "<test></test>";
        String expectedAckXml = "<ackTest></ackTest>";
        String expectedFilename = "TestFilename.xml";
        int expectedRecordsSent = 2;
        String s = """
                                {
                                    "recordsSent": %s,
                                    "concorContributionIds": [%s],
                                    "xmlContent" : "%s",
                                    "ackXmlContent" : "%s",
                                    "xmlFileName" : "%s"
                                }""";
        s = s.formatted( expectedRecordsSent, savedEntityId3, expectedXml, expectedAckXml, expectedFilename);
        mockMvc.perform(MockMvcRequestBuilders.post(ATOMIC_UPDATE_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // verify the content of the saved entity.
        ContributionFilesEntity savedEntity = contributionFileEntityCaptor.getValue();
        assertNotNull(savedEntity);
        assertNotNull(savedEntity.getId());
        assertEquals(expectedRecordsSent, savedEntity.getRecordsSent());
        assertEquals(expectedXml, savedEntity.getXmlContent());
        assertEquals(expectedAckXml, savedEntity.getAckXmlContent());
        assertEquals(expectedFilename, savedEntity.getFileName());
        // assert the file id has been set on the contribution
        Optional<ConcorContributionsEntity> updatedConcor = concorRepository.findById(savedEntityId3);
        assertTrue(updatedConcor.isPresent());
        ConcorContributionsEntity fdcContributionsEntity = updatedConcor.get();
        assertEquals(savedEntity.getId(), fdcContributionsEntity.getContribFileId());
        assertEquals(ConcorContributionStatus.SENT, fdcContributionsEntity.getStatus());
    }

    @Test
    void testLogDrcProcessedNoErrors() throws Exception {
        String s = """
                    {
                        "concorId" : %s,
                        "errorText" : "%s"
                    }""".formatted(savedEntityId1,"");

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check we've updated the received count.
        Optional<ContributionFilesEntity> fileEntity = repos.contributionFiles.findById(file1Id);
        assertTrue(fileEntity.isPresent());
        assertEquals(1,fileEntity.get().getRecordsReceived());

        // check no values in errors
        assertEquals(0, repos.contributionFileErrors.count());
    }

    @Test
    void testLogDrcProcessedWithErrors() throws Exception {
        String errorText = "ErrorText";
        String s = """
                    {
                        "concorId" : %s,
                        "errorText" : "%s"
                    }""".formatted(savedEntityId1,errorText);

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check we've updated the received count.
        Optional<ContributionFilesEntity> fileEntity = repos.contributionFiles.findById(file1Id);
        assertTrue(fileEntity.isPresent());
        assertEquals(1,fileEntity.get().getRecordsReceived());

        // check no values in errors
        assertEquals(1, repos.contributionFileErrors.count());
        ContributionFileErrorsEntity errorEntity = repos.contributionFileErrors.findAll().get(0);
        assertEquals(savedEntityId1, errorEntity.getConcorContributionId());
        assertEquals(savedEntityId1, errorEntity.getContributionId());
        assertEquals(errorText, errorEntity.getErrorText());
        assertEquals(file1Id, errorEntity.getContributionFileId());
        assertEquals(LocalDate.now(), errorEntity.getDateCreated());

    }

    @Test
    void testLogDrcProcessedNoFile() throws Exception {
        String s = """
                    {
                        "concorId" : %s,
                        "errorText" : "%s"
                    }""".formatted(savedEntityId3,"");

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check no values in errors
        assertEquals(0, repos.contributionFileErrors.count());
    }


}