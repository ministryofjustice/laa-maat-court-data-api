package gov.uk.courtdata.integration.iojAppeal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.IOJAppealEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.time.format.DateTimeFormatter;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.enums.CurrentStatus;
import uk.gov.justice.laa.crime.enums.IojAppealAssessor;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
@ExtendWith(SoftAssertionsExtension.class)
class IojAppealControllerIntegrationTest extends MockMvcIntegrationTest {

  private static final String ENDPOINT_URL = "/api/internal/v2/assessment/ioj-appeals";
  private static final String TRACE_ID = "abc123456789";
  private int repId;

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
  void givenLegacyIojAppealId_whenGetIOJAppealIsInvoked_thenApiGetIojAppealResponseIsReturned() throws Exception {
    IOJAppealEntity iojAppealEntity = repos.iojAppeal.save(TestEntityDataBuilder.getIojAppealEntity("COMPLETE", repId));
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
        .andExpect(jsonPath("$.appealAssessor").value(IojAppealAssessor.CASEWORKER.toString()))
        .andExpect(jsonPath("$.appealSuccessful").value(true))
        .andExpect(jsonPath("$.decisionReason").value(iojAppealEntity.getIderCode()))
        .andExpect(jsonPath("$.notes").value(iojAppealEntity.getNotes()))
        .andExpect(jsonPath("$.decisionDate").value(decisionDate));
  }

  @Test
  void givenApiCreateIojAppealRequest_whenCreateIOJAppealIsInvoked_thenIojAppealIsSaved() throws Exception {
    ApiCreateIojAppealRequest apiCreateIojAppealRequest = TestModelDataBuilder.getApiCreateIojAppealRequest(repId);
    String apiCreateIojAppealJson = objectMapper.writeValueAsString(apiCreateIojAppealRequest);

    mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
        .content(apiCreateIojAppealJson)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.legacyAppealId").exists());

    IOJAppealEntity iojAppealEntity = repos.iojAppeal.findByRepId(repId);
    softly.assertThat(iojAppealEntity.getAppealSetupDate().toLocalDate()).isEqualTo(apiCreateIojAppealRequest.getIojAppeal().getReceivedDate());
    softly.assertThat(iojAppealEntity.getNworCode()).isEqualTo(apiCreateIojAppealRequest.getIojAppeal().getAppealReason().getCode());
    softly.assertThat(iojAppealEntity.getDecisionResult()).isEqualTo("PASS");
    softly.assertThat(iojAppealEntity.getIderCode()).isEqualTo(apiCreateIojAppealRequest.getIojAppeal().getDecisionReason().getCode());
    softly.assertThat(iojAppealEntity.getNotes()).isEqualTo(apiCreateIojAppealRequest.getIojAppeal().getNotes());
    softly.assertThat(iojAppealEntity.getDecisionDate().toLocalDate()).isEqualTo(apiCreateIojAppealRequest.getIojAppeal().getDecisionDate());
    softly.assertThat(iojAppealEntity.getRepOrder().getId()).isEqualTo(apiCreateIojAppealRequest.getIojAppealMetadata().getLegacyApplicationId());
    softly.assertThat(iojAppealEntity.getCmuId()).isEqualTo(apiCreateIojAppealRequest.getIojAppealMetadata().getCaseManagementUnitId());
    softly.assertThat(iojAppealEntity.getUserCreated()).isEqualTo(apiCreateIojAppealRequest.getIojAppealMetadata().getUserSession().getUserName());
    softly.assertThat(iojAppealEntity.getIapsStatus()).isEqualTo("COMPLETE");
    softly.assertThat(iojAppealEntity.getAppealSetupResult()).isEqualTo("GRANT");
  }

  @Test
  void givenApiCreateIojAppealRequestWithMissingLegacyApplicationId_whenCreateIOJAppealIsInvoked_thenProblemDetailIsReturned()
      throws Exception {
    ApiCreateIojAppealRequest apiCreateIojAppealRequest = TestModelDataBuilder.getApiCreateIojAppealRequest(repId);
    apiCreateIojAppealRequest.getIojAppealMetadata().setLegacyApplicationId(null);
    String apiCreateIojAppealJson = objectMapper.writeValueAsString(apiCreateIojAppealRequest);
    MDC.put("traceId", TRACE_ID);
    
    mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
            .content(apiCreateIojAppealJson)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.type").value("about:blank"))
        .andExpect(jsonPath("$.title").value("Bad Request"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.detail").value("Validation Failure"))
        .andExpect(jsonPath("$.instance").value("/api/internal/v2/assessment/ioj-appeals"))
        .andExpect(jsonPath("$.errors.code").value("VALIDATION_FAILURE"))
        .andExpect(jsonPath("$.errors.traceId").value(TRACE_ID))
        .andExpect(jsonPath("$.errors.errors[0].field").value("Legacy Application Id"))
        .andExpect(jsonPath("$.errors.errors[0].message").value("Legacy Application Id is missing."));
    
    MDC.clear();
  }

  @Test
  void givenValidIoJAppealId_whenRollbackIoJAppealIsInvoked_thenIoJAppealIsRolledBack() throws Exception {
    IOJAppealEntity iojAppealEntity = repos.iojAppeal.save(TestEntityDataBuilder.getIojAppealEntity("COMPLETE", repId));
    Integer iojAppealId = iojAppealEntity.getId();

    mockMvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL + "/rollback/" + iojAppealId))
        .andExpect(status().isOk());

    IOJAppealEntity rolledBackIoJAppealEntity = repos.iojAppeal.findByRepId(repId);
    assertThat(rolledBackIoJAppealEntity.getIapsStatus()).isEqualTo(CurrentStatus.IN_PROGRESS.getStatus());
  }
}
