package gov.uk.courtdata.integration.dces;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.entity.FdcContributionsEntity;
import gov.uk.courtdata.enums.FdcContributionsStatus;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.repository.FdcContributionsRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FdcContributionsIntegrationTest extends MockMvcIntegrationTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/fdc-contribution-files?status=";
    private static final int expectedId1 = 1;
    private static final String expectedFinalCost1 = "1111.11";
    private static final FdcContributionsStatus expectedStatus1 = FdcContributionsStatus.REQUESTED;
    private static final int expectedId2 = 2;
    private static final String expectedFinalCost2 = "2222.22";
    private static final FdcContributionsStatus expectedStatus2 = FdcContributionsStatus.REQUESTED;
    private static final int expectedId3 = 3;
    private static final String expectedFinalCost3 = "3333.33";
    private static final FdcContributionsStatus expectedStatus3 = FdcContributionsStatus.INVALID;
    private static final int expectedId4 = 4;
    private static final String expectedFinalCost4 = "4444.44";
    private static final FdcContributionsStatus expectedStatus4 = FdcContributionsStatus.SENT;


    @AfterAll
    public static void clearUp(@Autowired FdcContributionsRepository fdcContributionsRepository) {
        RepositoryUtil.clearUp(fdcContributionsRepository);
    }

    @BeforeAll
    public static void setUp(@Autowired FdcContributionsRepository fdcContributionsRepository) {
        fdcContributionsRepository.saveAll(List.of(
                FdcContributionsEntity.builder().id(expectedId1).status(expectedStatus1).finalCost(BigDecimal.valueOf(Double.parseDouble(expectedFinalCost1))).build(),
                FdcContributionsEntity.builder().id(expectedId2).status(expectedStatus2).finalCost(BigDecimal.valueOf(Double.parseDouble(expectedFinalCost2))).build(),
                FdcContributionsEntity.builder().id(expectedId3).status(expectedStatus3).finalCost(BigDecimal.valueOf(Double.parseDouble(expectedFinalCost3))).build(),
                FdcContributionsEntity.builder().id(expectedId4).status(expectedStatus4).finalCost(BigDecimal.valueOf(Double.parseDouble(expectedFinalCost4))).build()
        ));
    }

    @Test
    @Order(1)
    void givenREQUESTEDStatus_whenGetIsInvoked_theDataLoadedResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+FdcContributionsStatus.REQUESTED)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fdcContributions.length()").value(2))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id=="+expectedId1+")].id").exists())
                .andExpect(jsonPath("$.fdcContributions.[?(@.id=="+expectedId1+")].finalCost").value(Double.parseDouble(expectedFinalCost1)))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id=="+expectedId2+")].id").exists())
                .andExpect(jsonPath("$.fdcContributions.[?(@.id=="+expectedId2+")].finalCost").value(Double.parseDouble(expectedFinalCost2)));
    }

    @Test
    @Order(3)
    void givenINVALIDStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+FdcContributionsStatus.INVALID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fdcContributions.length()").value(1))
                .andExpect(jsonPath("$.fdcContributions.[?(@.id=="+expectedId3+")].id").exists())
                .andExpect(jsonPath("$.fdcContributions.[?(@.id=="+expectedId3+")].finalCost").value(Double.parseDouble(expectedFinalCost3)));
    }

    @Test
    @Order(2)
    void givenAnInvalidStatus_whenGetIsInvoked_theEmptyResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL+"RUBBISH_VALUE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("code").value("BAD_REQUEST"))
                .andExpect(jsonPath("message").value("The provided value 'RUBBISH_VALUE' is the incorrect type for the 'status' parameter."));
    }

}