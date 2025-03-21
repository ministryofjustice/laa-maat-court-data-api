package gov.uk.courtdata.integration.repOrder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import gov.uk.courtdata.util.ApiHeaders;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
class CCOutcomeControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final Integer INVALID_REP_ID = 999999;
    private static final Integer INVALID_OUTCOME_ID = -1;
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/cc-outcome";

    private Integer repID;
    private Integer mockRepId1;
    private Integer mockRepId2;
    private RepOrderEntity repOrderEntity;
    private RepOrderEntity existingRepOrder;

    @InjectSoftAssertions
    private SoftAssertions softly;


    @BeforeEach
    void setUp() {
        existingRepOrder = repos.repOrder.saveAndFlush(
            TestEntityDataBuilder.getPopulatedRepOrder());
        repID = existingRepOrder.getId();
        repOrderEntity = repos.repOrder.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder());
        mockRepId1 = repOrderEntity.getId();
        RepOrderEntity repOrder = repos.repOrder.saveAndFlush(
                TestEntityDataBuilder.getPopulatedRepOrder());
        mockRepId2 = repOrder.getId();
    }

    @AfterEach
    void clearUp() {
        existingRepOrder = null;
    }

    @Test
    void givenAInvalidData_whenCreateIsInvoked_thenErrorIsThrown() throws Exception {
        RepOrderCCOutcome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutcome();
        repOrderCCOutCome.setUserCreated(null);
        repOrderCCOutCome.setId(null);
        assertTrue(runBadRequestErrorScenario("User created is required",
                post(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(repOrderCCOutCome))));
    }

    @Test
    void givenAValidData_whenCreateIsInvoked_thenCreateOutcomeIsSuccess() throws Exception {
        RepOrderCCOutcome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutcome();
        repOrderCCOutCome.setId(null);
        repOrderCCOutCome.setRepId(repID);
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(repOrderCCOutCome))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.repId").value(repID));
    }

    @Test
    void givenAInValidOutcomeId_whenUpdateIsInvoked_thenThrowException() throws Exception {
        RepOrderCCOutcome repOrderCCOutcome = TestModelDataBuilder.getUpdateRepOrderCCOutcome(INVALID_OUTCOME_ID,
                repID);
        repOrderCCOutcome.setRepId(repID);
        assertTrue(runNotFoundErrorScenario("No CC Outcome found for ID: -1",
                MockMvcRequestBuilders.put(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(repOrderCCOutcome)))
        );
    }

    @Test
    void givenAValidData_whenUpdateIsInvoked_thenShouldUpdatedCCOutCome() throws Exception {
        RepOrderCCOutComeEntity savedOutcome = repos.crownCourtProcessing.saveAndFlush(
            TestEntityDataBuilder.getRepOrderCCOutcomeEntity(1, existingRepOrder));
        runSuccessScenario(MockMvcRequestBuilders.put(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateRepOrderCCOutcome(savedOutcome.getId(),
                        repID))));
        RepOrderCCOutComeEntity ccOutCome = repos.crownCourtProcessing.findById(
            savedOutcome.getId()).get();
        softly.assertThat(repID).isEqualTo(ccOutCome.getRepOrder().getId());
        softly.assertThat(TestModelDataBuilder.TEST_CASE_ID.toString()).isEqualTo(ccOutCome.getCaseNumber());
        softly.assertThat("430").isEqualTo(ccOutCome.getCrownCourtCode());
        softly.assertThat(TestModelDataBuilder.TEST_USER).isEqualTo(ccOutCome.getUserModified());
        softly.assertThat(ccOutCome.getDateModified()).isNotNull();
    }

    @Test
    void givenAInvalidRepId_whenFindIsInvoked_thenReturnValidationException() throws Exception {
        runBadRequestErrorScenario("MAAT/REP ID [999999] is invalid",
                MockMvcRequestBuilders.get(ENDPOINT_URL + "/reporder/" + INVALID_REP_ID));
    }

    @Test
    void givenARepIdNotFoundInOutcome_whenFindIsInvoked_thenReturnEmpty() throws Exception {
        runSuccessScenario(List.of(), MockMvcRequestBuilders.get(ENDPOINT_URL + "/reporder/" + repID));
    }

    @Test
    void givenAValidRepId_whenFindIsInvoked_thenReturnOutcome() throws Exception {
        repos.crownCourtProcessing.saveAndFlush(
            TestEntityDataBuilder.getRepOrderCCOutcomeEntity(2, existingRepOrder));
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/reporder/" + repID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].repId").value(repID))
                .andExpect(jsonPath("$[0].dateCreated").isNotEmpty())
                .andExpect(jsonPath("$[0].dateModified").isNotEmpty());
    }

    @Test
    void givenAValidRepId_whenFindIsInvoked_thenReturnOutcomeCount() throws Exception {
        RepOrderCCOutComeEntity repOrderCCOutComeEntity = TestEntityDataBuilder.getRepOrderCCOutcomeEntity(3, repOrderEntity);
        repos.crownCourtProcessing.saveAndFlush(repOrderCCOutComeEntity);
        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.head(
                ENDPOINT_URL + "/reporder/" + mockRepId1));
        assertThat(result.getResponse().getHeader(ApiHeaders.TOTAL_RECORDS)).isEqualTo("1");
    }

    @Test
    void givenAEmptyRepIdInRepOrderOutcome_whenFindIsInvoked_thenReturnOutcomeCountAsZero() throws Exception {
        RepOrderCCOutComeEntity repOrderCCOutComeEntity = TestEntityDataBuilder.getRepOrderCCOutcomeEntity(4, repOrderEntity);
        repos.crownCourtProcessing.saveAndFlush(repOrderCCOutComeEntity);
        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.head(
                ENDPOINT_URL + "/reporder/" + mockRepId2));
        assertThat(result.getResponse().getHeader(ApiHeaders.TOTAL_RECORDS)).isEqualTo("0");
    }
}
