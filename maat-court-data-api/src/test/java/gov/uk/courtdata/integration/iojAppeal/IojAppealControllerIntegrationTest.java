package gov.uk.courtdata.integration.iojAppeal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.advice.ProblemDetailError;
import java.time.format.DateTimeFormatter;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.enums.CurrentStatus;
import uk.gov.justice.laa.crime.enums.IojAppealAssessor;
import uk.gov.justice.laa.crime.enums.NewWorkReason;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
class IojAppealControllerIntegrationTest extends MockMvcIntegrationTest {

    private int repId;
    private static final String ENDPOINT_URL = "/api/internal/v2/assessment/ioj-appeals";

    @Autowired
    private ObjectMapper objectMapper;

    @InjectSoftAssertions
    private SoftAssertions softly;

    @BeforeEach
    void setUp() {
        RepOrderEntity repOrderEntity = repos.repOrder.saveAndFlush(
                TestEntityDataBuilder.getPopulatedRepOrder());
        repId = repOrderEntity.getId();
    }

    @Test
    void givenLegacyIojAppealId_whenGetIojAppealIsInvoked_thenApiGetIojAppealResponseIsReturned()
            throws Exception {
        IOJAppealEntity iojAppealEntity = repos.iojAppeal.save(
                TestEntityDataBuilder.getIojAppealEntity("COMPLETE", repId));
        int legacyIojAppealId = iojAppealEntity.getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String appealSetupDate = iojAppealEntity.getAppealSetupDate().format(formatter);
        String decisionDate = iojAppealEntity.getDecisionDate().format(formatter);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + legacyIojAppealId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.legacyAppealId").value(legacyIojAppealId))
                .andExpect(jsonPath("$.receivedDate").value(appealSetupDate))
                .andExpect(jsonPath("$.appealReason").value(iojAppealEntity.getNworCode()))
                .andExpect(
                        jsonPath("$.appealAssessor").value(IojAppealAssessor.CASEWORKER.toString()))
                .andExpect(jsonPath("$.appealSuccessful").value(true))
                .andExpect(jsonPath("$.decisionReason").value(iojAppealEntity.getIderCode()))
                .andExpect(jsonPath("$.notes").value(iojAppealEntity.getNotes()))
                .andExpect(jsonPath("$.decisionDate").value(decisionDate));
    }

    @Test
    void givenNonExistentLegacyIojAppealId_whenGetIojAppealIsInvoked_thenNotFoundProblemDetailIsReturned()
            throws Exception {

        int nonExistentIojAppealId = Integer.MAX_VALUE;

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + nonExistentIojAppealId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail")
                        .value("No IoJ Appeal found for ID: " + nonExistentIojAppealId))
                .andExpect(
                        jsonPath("$.instance").value(ENDPOINT_URL + "/" + nonExistentIojAppealId))
                .andExpect(
                        jsonPath("$.errors.code").value(ProblemDetailError.OBJECT_NOT_FOUND.code()))
                .andExpect(jsonPath("$.errors.errors").isArray())
                .andExpect(jsonPath("$.errors.errors").isEmpty());
    }

    @Test
    void givenApiCreateIojAppealRequest_whenCreateIOJAppealIsInvoked_thenIojAppealIsSaved()
            throws Exception {
        ApiCreateIojAppealRequest apiCreateIojAppealRequest = TestModelDataBuilder.getApiCreateIojAppealRequest(
                repId);
        String apiCreateIojAppealJson = objectMapper.writeValueAsString(apiCreateIojAppealRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(apiCreateIojAppealJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.legacyAppealId").exists())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        IOJAppealEntity iojAppealEntity = repos.iojAppeal.findByRepId(repId);
        assertThat(iojAppealEntity).isNotNull();

        softly.assertThat(iojAppealEntity.getAppealSetupDate().toLocalDate())
                .isEqualTo(apiCreateIojAppealRequest.getIojAppeal().getReceivedDate());
        softly.assertThat(iojAppealEntity.getNworCode())
                .isEqualTo(apiCreateIojAppealRequest.getIojAppeal().getAppealReason().getCode());
        softly.assertThat(iojAppealEntity.getDecisionResult()).isEqualTo("PASS");
        softly.assertThat(iojAppealEntity.getIderCode())
                .isEqualTo(apiCreateIojAppealRequest.getIojAppeal().getDecisionReason().getCode());
        softly.assertThat(iojAppealEntity.getNotes())
                .isEqualTo(apiCreateIojAppealRequest.getIojAppeal().getNotes());
        softly.assertThat(iojAppealEntity.getDecisionDate().toLocalDate())
                .isEqualTo(apiCreateIojAppealRequest.getIojAppeal().getDecisionDate());
        softly.assertThat(iojAppealEntity.getRepOrder().getId()).isEqualTo(
                apiCreateIojAppealRequest.getIojAppealMetadata().getLegacyApplicationId());
        softly.assertThat(iojAppealEntity.getCmuId()).isEqualTo(
                apiCreateIojAppealRequest.getIojAppealMetadata().getCaseManagementUnitId());
        softly.assertThat(iojAppealEntity.getUserCreated()).isEqualTo(
                apiCreateIojAppealRequest.getIojAppealMetadata().getUserSession().getUserName());
        softly.assertThat(iojAppealEntity.getIapsStatus()).isEqualTo("COMPLETE");
        softly.assertThat(iojAppealEntity.getAppealSetupResult()).isEqualTo("GRANT");
    }

    @Test
    void givenCreateIojAppealRequestWithInvalidAppealReason_whenCreateIojAppealIsInvoked_thenValidationProblemDetailIsReturned()
            throws Exception {
        ApiCreateIojAppealRequest apiCreateIojAppealRequest = TestModelDataBuilder.getApiCreateIojAppealRequest(
                repId);
        apiCreateIojAppealRequest.getIojAppeal().setAppealReason(NewWorkReason.CFC);
        String apiCreateIojAppealJson = objectMapper.writeValueAsString(apiCreateIojAppealRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(apiCreateIojAppealJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(
                        ProblemDetailError.VALIDATION_FAILURE.defaultDetail()))
                .andExpect(jsonPath("$.instance").value("/api/internal/v2/assessment/ioj-appeals"))
                .andExpect(jsonPath("$.errors.code").value("VALIDATION_FAILURE"))
                .andExpect(jsonPath("$.errors.errors[0].field").value("Appeal reason"))
                .andExpect(jsonPath("$.errors.errors[0].message").value(
                        "Appeal Reason Is Invalid."));
    }

    @Test
    void givenMalformedJson_whenCreateIojAppealIsInvoked_thenBadRequestProblemDetailIsReturned()
            throws Exception {

        String malformedJson = """
                {
                  "iojAppealMetadata": {
                    "legacyApplicationId": 123
                  },
                  "iojAppeal": {
                    "receivedDate": "2025-01-01",
                    "appealReason": {
                      "code": "M"
                    }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(malformedJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(
                        jsonPath("$.detail").value(ProblemDetailError.BAD_REQUEST.defaultDetail()))
                .andExpect(jsonPath("$.instance").value(ENDPOINT_URL))
                .andExpect(jsonPath("$.errors.code").value(ProblemDetailError.BAD_REQUEST.code()))
                .andExpect(jsonPath("$.errors.errors").isArray())
                .andExpect(jsonPath("$.errors.errors").isEmpty());
    }

    @Test
    void givenMissingRequestBody_whenCreateIojAppealIsInvoked_thenBadRequestProblemDetailIsReturned()
            throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(
                        jsonPath("$.detail").value(ProblemDetailError.BAD_REQUEST.defaultDetail()))
                .andExpect(jsonPath("$.instance").value(ENDPOINT_URL))
                .andExpect(jsonPath("$.errors.code").value(ProblemDetailError.BAD_REQUEST.code()))
                .andExpect(jsonPath("$.errors.errors").isArray())
                .andExpect(jsonPath("$.errors.errors").isEmpty());
    }

    @Test
    void givenValidIoJAppealId_whenRollbackIoJAppealIsInvoked_thenIoJAppealIsRolledBack()
            throws Exception {
        IOJAppealEntity iojAppealEntity = repos.iojAppeal.save(
                TestEntityDataBuilder.getIojAppealEntity("COMPLETE", repId));
        Integer iojAppealId = iojAppealEntity.getId();

        mockMvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/rollback/" + iojAppealId))
                .andExpect(status().isOk());

        IOJAppealEntity rolledBackIoJAppealEntity = repos.iojAppeal.findByRepId(repId);
        assertThat(rolledBackIoJAppealEntity).isNotNull();
        assertThat(rolledBackIoJAppealEntity.getIapsStatus()).isEqualTo(
                CurrentStatus.IN_PROGRESS.getStatus());
    }

    @Test
    void givenNonExistentIojAppealId_whenRollbackIojAppealIsInvoked_thenNotFoundProblemDetailIsReturned()
            throws Exception {

        int nonExistentIojAppealId = Integer.MAX_VALUE;

        mockMvc.perform(MockMvcRequestBuilders.patch(
                        ENDPOINT_URL + "/rollback/" + nonExistentIojAppealId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.type").value("about:blank"))
                .andExpect(jsonPath("$.title").value("Not Found"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail")
                        .value("No IoJ Appeal found for ID: " + nonExistentIojAppealId))
                .andExpect(jsonPath("$.instance")
                        .value(ENDPOINT_URL + "/rollback/" + nonExistentIojAppealId))
                .andExpect(
                        jsonPath("$.errors.code").value(ProblemDetailError.OBJECT_NOT_FOUND.code()))
                .andExpect(jsonPath("$.errors.errors").isArray())
                .andExpect(jsonPath("$.errors.errors").isEmpty());
    }
}
