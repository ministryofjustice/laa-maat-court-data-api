package gov.uk.courtdata.integration.dces;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.dces.service.DebtCollectionRepository;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class FdcContributionsIntegrationTest extends MockMvcIntegrationTest {

    private static final String FDC_CONTRIBUTION_FILES_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/fdc-contribution-files";
    private static final String GLOBAL_UPDATE_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/prepare-fdc-contributions-files";
    private static final String ATOMIC_UPDATE_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/create-fdc-file";
    private static final String DRC_UPDATE_URL = "/api/internal/v1/debt-collection-enforcement/log-fdc-response";

    private static final String expectedFinalCost1 = "1111.11";
    private static final FdcContributionsStatus expectedStatus1 = FdcContributionsStatus.REQUESTED;
    private static final String expectedFinalCost2 = "2222.22";
    private static final FdcContributionsStatus expectedStatus2 = FdcContributionsStatus.REQUESTED;
    private static final String expectedFinalCost3 = "3333.33";
    private static final FdcContributionsStatus expectedStatus3 = FdcContributionsStatus.INVALID;
    private static final String expectedFinalCost4 = "4444.44";
    private static final FdcContributionsStatus expectedStatus4 = FdcContributionsStatus.SENT;
    private static int expectedId1 = 1;
    private static int expectedId2 = 2;
    private static int expectedId3 = 3;
    private static int expectedId4 = 4;
    private int repId1 = 1;
    private int repId2 = 2;
    private int repId3 = 3;
    private int repId4 = 4;

    private static final String fileOne = "FileOne";
    private static int file1Id;
    private static final String fileTwo = "FileTwo";
    private static int file2Id;

    @SpyBean
    DebtCollectionRepository debtCollectionRepositorySpy;
    @Captor
    private ArgumentCaptor<ContributionFilesEntity> contributionFileEntityCaptor;


    @BeforeEach
    public void setUp() {
        file1Id = repos.contributionFiles.save(buildFileEntity(fileOne, false)).getFileId();
        file2Id = repos.contributionFiles.save(buildFileEntity(fileTwo, true)).getFileId();

        expectedId1 = repos.fdcContributions.save(buildFdcEntity(expectedStatus1, expectedFinalCost1, file1Id)).getId();
        repId1 = expectedId1;
        expectedId2 = repos.fdcContributions.save(buildFdcEntity(expectedStatus2, expectedFinalCost2, file1Id)).getId();
        repId2 = expectedId2;
        expectedId3 = repos.fdcContributions.save(buildFdcEntity(expectedStatus3, expectedFinalCost3, null)).getId();
        repId3 = expectedId3;
        expectedId4 = repos.fdcContributions.save(buildFdcEntity(expectedStatus4, expectedFinalCost4, file2Id)).getId();
        repId4 = expectedId4;
    }

    private ContributionFilesEntity buildFileEntity(String fileIdentifier, boolean isBlankXml){
        ContributionFilesEntity entity = ContributionFilesEntity.builder()
                .fileName(fileIdentifier)
                .recordsReceived(0)
                .recordsSent(10)
                .build();
        if(!isBlankXml){
            entity.setXmlContent("<xml>"+fileIdentifier+"</xml>");
            entity.setAckXmlContent("<ackXml>"+fileIdentifier+"</ackXml>");
        }
        return entity;
    }

    private FdcContributionsEntity buildFdcEntity(FdcContributionsStatus status, String finalCost, Integer fileId) {
        BigDecimal finalCostBigDecimal = new BigDecimal(finalCost);
        return FdcContributionsEntity.builder()
                .status(status)
                .repOrderEntity(repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder()))
                .finalCost(finalCostBigDecimal)
                .contFileId(fileId)
                .build();
    }

    @Test
    void givenREQUESTEDStatus_whenGetIsInvoked_theDataLoadedResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(FDC_CONTRIBUTION_FILES_ENDPOINT_URL)
                        .queryParam("status", FdcContributionsStatus.REQUESTED.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fdcContributions.length()").value(2))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedId1 + ")].id").exists())
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedId1 + ")].finalCost").value(Double.parseDouble(expectedFinalCost1)))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedId2 + ")].id").exists())
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedId2 + ")].finalCost").value(Double.parseDouble(expectedFinalCost2)));
    }

    @Test
    void givenINVALIDStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(FDC_CONTRIBUTION_FILES_ENDPOINT_URL)
                        .queryParam("status", FdcContributionsStatus.INVALID.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fdcContributions.length()").value(1))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedId3 + ")].id").exists())
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedId3 + ")].finalCost").value(Double.parseDouble(expectedFinalCost3)));
    }

    @Disabled("Disabled as testing this requires an immense amount of setup, and the underlying database for the test has issues with the sql grammar. It generates bad sql when running. Has been manually tested on the MAAT database, with the sql proven via that way.")
    @Test
    void testGlobalUpdate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(GLOBAL_UPDATE_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAnInvalidStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(FDC_CONTRIBUTION_FILES_ENDPOINT_URL)
                        .queryParam("status", "RUBBISH_VALUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("The provided value 'RUBBISH_VALUE' is the incorrect type for the 'status' parameter."));
    }

    @Test
    void givenAListOfIds_whenAtomicUpdate_theStatusIsUpdated() throws Exception {
        // removed the xml values as it cannot be tested due to xmlType. The double refuses to handle XMLTYPE(?) as that is oracle specific.
        String expectedFilename = "TestFilename.xml";
        int expectedFileListSize = repos.contributionFiles.findAll().size()+1;
        int expectedRecordsSent = 2;
        String s = """
                                {
                                    "recordsSent": %s,
                                    "fdcIds": [%s],
                                    "xmlFileName" : "%s"
                                }""";
        s = s.formatted( expectedRecordsSent, expectedId4, expectedFilename);
        mockMvc.perform(MockMvcRequestBuilders.post(ATOMIC_UPDATE_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // verify the content of the saved entity.
        List<ContributionFilesEntity> savedFileEntities = repos.contributionFiles.findAll();
        assertEquals(expectedFileListSize, savedFileEntities.size());
        ContributionFilesEntity savedFileEntity = savedFileEntities.get(expectedFileListSize-1);
        assertNotNull(savedFileEntity);
        assertNotNull(savedFileEntity.getFileId());
        assertEquals(expectedRecordsSent, savedFileEntity.getRecordsSent());
        assertNull(savedFileEntity.getXmlContent()); // unfortunately, cannot test due to aforementioned XMLTYPE() issue.
        assertNull(savedFileEntity.getAckXmlContent());
        assertEquals(expectedFilename, savedFileEntity.getFileName());
        // assert the file id has been set on the fdc
        Optional<FdcContributionsEntity> updatedFdc = repos.fdcContributions.findById(expectedId4);
        assertTrue(updatedFdc.isPresent());
        FdcContributionsEntity fdcContributionsEntity = updatedFdc.get();
        assertEquals(savedFileEntity.getFileId(), fdcContributionsEntity.getContFileId());
        assertEquals(FdcContributionsStatus.SENT, fdcContributionsEntity.getStatus());

    }



    @Test
    void testLogDrcProcessedNoErrors() throws Exception {
        String s = createLogDrcProcessedRequest(expectedId4,"");

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check we've updated the received count.
        Optional<ContributionFilesEntity> fileEntity = repos.contributionFiles.findById(file2Id);
        assertTrue(fileEntity.isPresent());
        assertEquals(1,fileEntity.get().getRecordsReceived());

        // check no values in errors
        assertEquals(0, repos.contributionFileErrors.count());
    }

    @Test
    void testLogDrcProcessedWithErrors() throws Exception {
        String errorText = "ErrorText";
        String s = createLogDrcProcessedRequest(expectedId4,errorText);
        LocalDateTime dateTimeCheck = LocalDateTime.now();

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check we've updated the received count.
        Optional<ContributionFilesEntity> fileEntity = repos.contributionFiles.findById(file2Id);
        assertTrue(fileEntity.isPresent());
        assertEquals(1,fileEntity.get().getRecordsReceived());

        // check no values in errors
        assertEquals(1, repos.contributionFileErrors.count());
        ContributionFileErrorsEntity errorEntity = repos.contributionFileErrors.findAll().get(0);
        assertEquals(expectedId4, errorEntity.getFdcContributionId());
        assertEquals(expectedId4, errorEntity.getContributionId());
        assertEquals(errorText, errorEntity.getErrorText());
        assertEquals(file2Id, errorEntity.getContributionFileId());

        assertEquals(dateTimeCheck.getDayOfMonth(), errorEntity.getDateCreated().getDayOfMonth());
        assertEquals(dateTimeCheck.getMonth(), errorEntity.getDateCreated().getMonth());
        assertEquals(dateTimeCheck.getYear(), errorEntity.getDateCreated().getYear());

    }

    @Test
    void testLogDrcProcessedNoFile() throws Exception {
        String s = createLogDrcProcessedRequest(expectedId4,"");

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check no values in errors
        assertEquals(0, repos.contributionFileErrors.count());
    }

    private String createLogDrcProcessedRequest(Integer id, String errorText){
        return  """
                    {
                        "fdcId" : %s,
                        "errorText" : "%s"
                    }""".formatted(id,errorText);
    }




}