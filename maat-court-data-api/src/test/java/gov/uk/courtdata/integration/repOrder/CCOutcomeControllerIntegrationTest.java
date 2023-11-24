package gov.uk.courtdata.integration.repOrder;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import gov.uk.courtdata.util.RepositoryUtil;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class CCOutcomeControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final String endpointUrl = "/api/internal/v1/assessment/rep-orders/cc-outcome";

    private static final Integer INVALID_REP_ID = 999999;
    private static final Integer INVALID_OUTCOME_ID = -1;
    @Autowired
    private CrownCourtProcessingRepository courtProcessingRepository;
    @Autowired
    private RepOrderRepository repOrderRepository;
    private RepOrderEntity existingRepOrder;
    @InjectSoftAssertions
    private SoftAssertions softly;

    @BeforeEach
    void setUp() {
        existingRepOrder = repOrderRepository.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID));
        repOrderRepository.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder(500));
        repOrderRepository.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder(501));
    }

    @AfterEach
    void clearUp() {
        existingRepOrder = null;
        RepositoryUtil.clearUp(courtProcessingRepository, repOrderRepository);
    }

    @Test
    void givenAInvalidData_whenCreateIsInvoked_thenErrorIsThrown() throws Exception {
        RepOrderCCOutcome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutcome();
        repOrderCCOutCome.setUserCreated(null);
        repOrderCCOutCome.setId(null);
        assertTrue(runBadRequestErrorScenario("User created is required",
                post(endpointUrl).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(repOrderCCOutCome))));
    }
    @Test
    void givenAValidData_whenCreateIsInvoked_thenCreateOutcomeIsSuccess() throws Exception {
        RepOrderCCOutcome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutcome();
        repOrderCCOutCome.setId(null);
        mockMvc.perform(MockMvcRequestBuilders.post(endpointUrl)
                        .content(objectMapper.writeValueAsString(repOrderCCOutCome))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.repId").value(REP_ID));
    }

    @Test
    void givenAInValidOutcomeId_whenUpdateIsInvoked_thenThrowException() throws Exception {
        assertTrue(runNotFoundErrorScenario("No CC Outcome found for ID: -1",
                MockMvcRequestBuilders.put(endpointUrl).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateRepOrderCCOutcome(INVALID_OUTCOME_ID))))
        );
    }

    @Test
    void givenAValidData_whenUpdateIsInvoked_thenShouldUpdatedCCOutCome() throws Exception {
        RepOrderCCOutComeEntity savedOutcome = courtProcessingRepository.saveAndFlush(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(1, TestModelDataBuilder.REP_ID));
        runSuccessScenario(MockMvcRequestBuilders.put(endpointUrl).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateRepOrderCCOutcome(savedOutcome.getId()))));
        RepOrderCCOutComeEntity ccOutCome = courtProcessingRepository.findById(savedOutcome.getId()).get();
        softly.assertThat(TestModelDataBuilder.REP_ID).isEqualTo(ccOutCome.getRepOrder().getId());
        softly.assertThat(TestModelDataBuilder.TEST_CASE_ID.toString()).isEqualTo(ccOutCome.getCaseNumber());
        softly.assertThat("430").isEqualTo(ccOutCome.getCrownCourtCode());
        softly.assertThat(TestModelDataBuilder.TEST_USER).isEqualTo(ccOutCome.getUserModified());
        softly.assertThat(ccOutCome.getDateModified()).isNotNull();
    }

    @Test
    void givenAInvalidRepId_whenFindIsInvoked_thenReturnValidationException() throws Exception {
        runBadRequestErrorScenario("MAAT/REP ID: 999999 is invalid.",
                MockMvcRequestBuilders.get(endpointUrl + "/reporder/" + INVALID_REP_ID));
    }

    @Test
    void givenARepIdNotFoundInOutcome_whenFindIsInvoked_thenReturnEmpty() throws Exception {
        runSuccessScenario(List.of(), MockMvcRequestBuilders.get(endpointUrl + "/reporder/" + REP_ID));
    }
    @Test
    void givenAValidRepId_whenFindIsInvoked_thenReturnOutcome() throws Exception {
        courtProcessingRepository.saveAndFlush(TestEntityDataBuilder.getRepOrderCCOutcomeEntity(2, REP_ID));
        mockMvc.perform(MockMvcRequestBuilders.get(endpointUrl + "/reporder/" + REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].repId").value(REP_ID))
                .andExpect(jsonPath("$[0].dateCreated").isNotEmpty())
                .andExpect(jsonPath("$[0].dateModified").isNotEmpty());
    }
    @Test
    void givenAValidRepId_whenFindIsInvoked_thenReturnOutcomeCount() throws Exception {
        RepOrderCCOutComeEntity repOrderCCOutComeEntity = TestEntityDataBuilder.getRepOrderCCOutcomeEntity(3, 500);
        courtProcessingRepository.saveAndFlush(repOrderCCOutComeEntity);
        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.head(endpointUrl + "/reporder/" + 500));
        assertThat(result.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("1");
    }

    @Test
    void givenAEmptyRepIdInRepOrderOutcome_whenFindIsInvoked_thenReturnOutcomeCountAsZero() throws Exception {
        RepOrderCCOutComeEntity repOrderCCOutComeEntity = TestEntityDataBuilder.getRepOrderCCOutcomeEntity(4, 500);
        courtProcessingRepository.saveAndFlush(repOrderCCOutComeEntity);
        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.head(endpointUrl + "/reporder/" + 501));
        assertThat(result.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("0");
    }
}
