package gov.uk.courtdata.integration.dces;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.dces.request.CreateFdcItemRequest;
import gov.uk.courtdata.dces.service.DebtCollectionRepository;
import gov.uk.courtdata.entity.ContributionFileErrorsEntity;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.entity.FdcItemsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class FdcContributionsIntegrationTest extends MockMvcIntegrationTest {
    private static final String FDC_CONTRIBUTION_FILES_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/fdc-contribution-files";
    private static final String FDC_CONTRIBUTION_FIND_BY_ID_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/fdc-contributions";
    private static final String GLOBAL_UPDATE_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/prepare-fdc-contributions-files";
    private static final String ATOMIC_UPDATE_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/create-fdc-file";
    private static final String DRC_UPDATE_URL = "/api/internal/v1/debt-collection-enforcement/log-fdc-response";
    private static final String FDC_ITEMS_URL = "/api/internal/v1/debt-collection-enforcement/fdc-items";

    private static final String EXPECTED_FINAL_COST_1 = "1111.11";
    private static final FdcContributionsStatus EXPECTED_STATUS_1 = FdcContributionsStatus.REQUESTED;
    private static final String EXPECTED_FINAL_COST_2 = "2222.22";
    private static final FdcContributionsStatus EXPECTED_STATUS_2 = FdcContributionsStatus.REQUESTED;
    private static final String EXPECTED_FINAL_COST_3 = "3333.33";
    private static final FdcContributionsStatus EXPECTED_STATUS_3 = FdcContributionsStatus.INVALID;
    private static final String EXPECTED_FINAL_COST_4 = "4444.44";
    private static final FdcContributionsStatus EXPECTED_STATUS_4 = FdcContributionsStatus.SENT;
    private static int expectedId1 = 1;
    private static int expectedId2 = 2;
    private static int expectedId3 = 3;
    private static int expectedId4 = 4;

    private static final String FILE_ONE = "FileOne";
    private static int file2Id;
    private static final String FILE_TWO = "FileTwo";

    @SpyBean
    DebtCollectionRepository debtCollectionRepositorySpy;

    @BeforeEach
    public void setUp() {
        int file1Id = repos.contributionFiles.save(buildFileEntity(FILE_ONE, false)).getFileId();
        file2Id = repos.contributionFiles.save(buildFileEntity(FILE_TWO, true)).getFileId();

        expectedId1 = repos.fdcContributions.save(buildFdcEntity(EXPECTED_STATUS_1, EXPECTED_FINAL_COST_1, file1Id)).getId();
        repos.fdcItemsRepository.save(FdcItemsEntity.builder().fdcId(expectedId1).build());
        expectedId2 = repos.fdcContributions.save(buildFdcEntity(EXPECTED_STATUS_2, EXPECTED_FINAL_COST_2, 99999999)).getId();
        expectedId3 = repos.fdcContributions.save(buildFdcEntity(EXPECTED_STATUS_3, EXPECTED_FINAL_COST_3, null)).getId();
        expectedId4 = repos.fdcContributions.save(buildFdcEntity(EXPECTED_STATUS_4, EXPECTED_FINAL_COST_4, file2Id)).getId();
    }

    private ContributionFilesEntity buildFileEntity(String fileIdentifier, boolean isBlankXml) {
        ContributionFilesEntity entity = ContributionFilesEntity.builder()
                .fileName(fileIdentifier)
                .recordsReceived(0)
                .recordsSent(10)
                .build();
        if (!isBlankXml){
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
                .agfsCost(finalCostBigDecimal)
                .lgfsCost(finalCostBigDecimal)
                .dateCalculated(TestEntityDataBuilder.TEST_DATE.toLocalDate())
                .contFileId(fileId)
                .build();
    }

    @Test
    void givenREQUESTEDStatus_whenGetIsInvoked_theDataLoadedResponseIsReturned() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(FDC_CONTRIBUTION_FILES_ENDPOINT_URL)
                        .queryParam("status", FdcContributionsStatus.REQUESTED.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fdcContributions.length()").value(2));
        validateFdcPresent(result,  expectedId1);
        validateFdcPresent(result,  expectedId2);
    }

    @Test
    void givenINVALIDStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(FDC_CONTRIBUTION_FILES_ENDPOINT_URL)
                        .queryParam("status", FdcContributionsStatus.INVALID.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        validateFdcPresent(result, expectedId3);
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
        s = s.formatted(expectedRecordsSent, expectedId4, expectedFilename);
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
        assertEquals(0, fileEntity.get().getRecordsReceived());

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
        String s = createLogDrcProcessedRequest(expectedId3,"");

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check no values in errors
        assertEquals(0, repos.contributionFileErrors.count());
    }


    @Test
    void testLogDrcProcessedMissingFile() throws Exception {
        String s = createLogDrcProcessedRequest(expectedId2,"");

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check no values in errors
        assertEquals(0, repos.contributionFileErrors.count());
    }

    @Test
    void testLogDrcProcessedTestRollback() throws Exception {
        int fdcId = expectedId4;
        String errorString = StringUtils.repeat("*", 5000);
        String s = createLogDrcProcessedRequest(fdcId,errorString);

        mockMvc.perform(MockMvcRequestBuilders.post(DRC_UPDATE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // check no values in errors
        assertEquals(0, repos.contributionFileErrors.count());

        FdcContributionsEntity originalFile = repos.fdcContributions.findById(fdcId).get();
        ContributionFilesEntity filesEntity = repos.contributionFiles.findById(originalFile.getContFileId()).get();
        assertEquals(0, filesEntity.getRecordsReceived()); // ensure the increment is rolled back.
    }

    @Test
    void testFindFdcById_WhereIdListIsValid() throws Exception {
        String s = String.format("[%s,%s]", expectedId1, expectedId4);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(FDC_CONTRIBUTION_FIND_BY_ID_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fdcContributions.length()").value(2));

                validateFdcPresent(result, expectedId1);
                validateFdcPresent(result, expectedId4);
    }

    @Test
    void testFindFdcById_WhereIdsStringProvided() throws Exception {
        String s = String.format("[%s,%s]", expectedId1,expectedId3);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post(FDC_CONTRIBUTION_FIND_BY_ID_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fdcContributions").exists())
                .andExpect(jsonPath("$.fdcContributions.length()").value(2));
        validateFdcPresent(result, expectedId1);
        validateFdcPresent(result, expectedId3);
    }

    @Test
    void testFindFdcById_WhereIdsNotFound() throws Exception {
        String s = String.format("[%s,%s]", -1111, 4444);
        mockMvc.perform(MockMvcRequestBuilders.post(FDC_CONTRIBUTION_FIND_BY_ID_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fdcContributions").exists())
                .andExpect(jsonPath("$.fdcContributions.length()").value(0));
    }

    @Test
    void testFindFdcById_WhereIdsNotProvided() throws Exception {
        String s = "[]";
        mockMvc.perform(MockMvcRequestBuilders.post(FDC_CONTRIBUTION_FIND_BY_ID_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().isBadRequest());
    }

    void validateFdcPresent(ResultActions result, Integer expectedFdcId) throws Exception {
        FdcContributionsEntity expectedEntity = repos.fdcContributions.findById(expectedFdcId).get();
        result.
                andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedFdcId + ")].id").exists())
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedFdcId + ")].maatId").value(expectedEntity.getRepOrderEntity().getId()))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedFdcId + ")].sentenceOrderDate").value(expectedEntity.getRepOrderEntity().getSentenceOrderDate().toString()))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedFdcId + ")].finalCost").value(expectedEntity.getFinalCost().doubleValue()))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedFdcId + ")].lgfsCost").value(expectedEntity.getLgfsCost().doubleValue()))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedFdcId + ")].agfsCost").value(expectedEntity.getAgfsCost().doubleValue()))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedFdcId + ")].dateCalculated").value(expectedEntity.getDateCalculated().toString()))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id==" + expectedFdcId + ")].status").value(expectedEntity.getStatus().toString()));
    }

    @Test
    void testDeleteFdcItem() throws Exception {
        long originalFdcItemCount = repos.fdcItemsRepository.count();
        String parameter = "/fdc-id/{fdc-id}";
        mockMvc.perform(MockMvcRequestBuilders.delete(FDC_ITEMS_URL+parameter, expectedId1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(originalFdcItemCount-1, repos.fdcItemsRepository.count());
    }

    @Test
    void testCreateFdcItemWithDefaultedValues() throws Exception {
        long originalFdcItemCount = repos.fdcItemsRepository.count();
        String itemType = "Test";
        CreateFdcItemRequest request = CreateFdcItemRequest.builder()
                .fdcId(expectedId2)
                .itemType(itemType)
                .build();

        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(FDC_ITEMS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(s))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(originalFdcItemCount+1, repos.fdcItemsRepository.count());
        Optional<FdcItemsEntity> optionalItem = repos.fdcItemsRepository.findAll().stream().filter(item-> item.getFdcId().equals(expectedId2)).findFirst();
        assertTrue(optionalItem.isPresent());
        FdcItemsEntity savedEntity = optionalItem.get();
        assertEquals(itemType, savedEntity.getItemType());
        // check basic values.
        assertEquals(LocalDate.now(), savedEntity.getDateCreated());
        assertEquals(LocalDate.now(), savedEntity.getDateModified());
        assertEquals("DCES", savedEntity.getUserCreated());
        assertEquals("DCES", savedEntity.getUserModified());
    }
    @Test
    void testCreateFdcItemWithExplicitValues() throws Exception {
        long originalFdcItemCount = repos.fdcItemsRepository.count();
        String itemType = "Test";
        LocalDate expectedDate = LocalDate.of(2000,1,1);
        String expectedUser = "Test User";
        String expectedCostInd = "A";
        String expectedPaidAsClaimed = "B";
        String expectedAdjustReason = "C";
        CreateFdcItemRequest request = CreateFdcItemRequest.builder()
                .fdcId(expectedId4)
                .itemType(itemType)
                .latestCostInd(expectedCostInd)
                .paidAsClaimed(expectedPaidAsClaimed)
                .adjustmentReason(expectedAdjustReason)
                .userCreated(expectedUser)
                .dateCreated(expectedDate)
                .build();

        ObjectMapper om = new ObjectMapper();
        om.findAndRegisterModules();
        String s = om.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post(FDC_ITEMS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertEquals(originalFdcItemCount+1, repos.fdcItemsRepository.count());
        Optional<FdcItemsEntity> optionalItem = repos.fdcItemsRepository.findAll().stream().filter(item-> item.getFdcId().equals(expectedId4)).findFirst();
        assertTrue(optionalItem.isPresent());
        FdcItemsEntity savedEntity = optionalItem.get();
        assertEquals(itemType, savedEntity.getItemType());
        // check basic values. Dates are auto-populated, so should ignore set values.
        assertEquals(LocalDate.now(), savedEntity.getDateCreated());
        assertEquals(LocalDate.now(), savedEntity.getDateModified());
        assertEquals(expectedUser, savedEntity.getUserCreated());
        assertEquals(expectedUser, savedEntity.getUserModified());
        assertEquals(expectedCostInd, savedEntity.getLatestCostInd());
        assertEquals(expectedPaidAsClaimed, savedEntity.getPaidAsClaimed());
        assertEquals(expectedAdjustReason, savedEntity.getAdjustmentReason());
    }

    private String createLogDrcProcessedRequest(Integer id, String errorText) {
        return  """
                    {
                        "fdcId" : %s,
                        "errorText" : "%s"
                    }""".formatted(id,errorText);
    }
}