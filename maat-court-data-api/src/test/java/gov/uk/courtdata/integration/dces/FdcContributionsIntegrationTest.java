package gov.uk.courtdata.integration.dces;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class FdcContributionsIntegrationTest extends MockMvcIntegrationTest {

    private static final String FDC_CONTRIBUTION_FILES_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/fdc-contribution-files";
    private static final String GLOBAL_UPDATE_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/prepare-fdc-contributions-files";
    private static final String ATOMIC_UPDATE_ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/create-fdc-file";
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

    @BeforeEach
    public void setUp() {
        expectedId1 = repos.fdcContributions.save(buildEntity(expectedStatus1, expectedFinalCost1)).getId();
        repId1 = expectedId1;
        expectedId2 = repos.fdcContributions.save(buildEntity(expectedStatus2, expectedFinalCost2)).getId();
        repId2 = expectedId2;
        expectedId3 = repos.fdcContributions.save(buildEntity(expectedStatus3, expectedFinalCost3)).getId();
        repId3 = expectedId3;
        expectedId4 = repos.fdcContributions.save(buildEntity(expectedStatus4, expectedFinalCost4)).getId();
        repId4 = expectedId4;
    }

    private FdcContributionsEntity buildEntity(FdcContributionsStatus status, String finalCost) {
        BigDecimal finalCostBigDecimal = new BigDecimal(finalCost);
        return FdcContributionsEntity.builder()
                .status(status)
                .repOrderEntity(repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder()))
                .finalCost(finalCostBigDecimal)
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


    @Disabled("Update SQL is not understood.")
    @Test
    void givenAListOfIds_whenAtomicUpdate_theStatusIsUpdated() throws Exception {
        String s = """
                                {
                                    "recordsSent": 2,
                                    "fdcIds": [%s],
                                    "xmlContent" : "<test></test>",
                                    "ackXmlContent" : "<ackTest></ackTest>",
                                    "xmlFileName" : "TestFilename.xml"
                                }""";
        s = s.formatted( expectedId4);

        mockMvc.perform(MockMvcRequestBuilders.post(ATOMIC_UPDATE_ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))

                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("The provided value 'RUBBISH_VALUE' is the incorrect type for the 'status' parameter."));
    }


}