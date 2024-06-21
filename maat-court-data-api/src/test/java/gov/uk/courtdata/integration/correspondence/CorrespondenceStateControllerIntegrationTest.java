package gov.uk.courtdata.integration.correspondence;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.CorrespondenceStateEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class CorrespondenceStateControllerIntegrationTest extends MockMvcIntegrationTest {


    private static final Integer INVALID_REP_ID = 9999;
    private static final Integer REP_ID = 12345;
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/correspondence-state";
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    MockMvc mvc;

    @BeforeEach
    public void setUp() {
        repos.correspondenceState.saveAndFlush(
            TestEntityDataBuilder.getCorrespondenceStateEntity(TestModelDataBuilder.REP_ID,
                "appealCC"));
    }

    @Test
    void givenAEmptyContent_whenCreateIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAValidContent_whenCreateIsInvoked_thenCorrectResponseIsReturned() throws Exception {

        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.post(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestEntityDataBuilder.getCorrespondenceStateEntity(REP_ID, "appealCC"))));
        Assertions.assertThat(
                objectMapper.writeValueAsString(repos.correspondenceState.findByRepId(REP_ID)))
                .isEqualTo(result.getResponse().getContentAsString());
    }


    @Test
    void givenAEmptyContent_whenUpdateIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAExistingRepId_whenUpdateIsInvoked_thenNewStatusShouldUpdated() throws Exception {

        CorrespondenceStateEntity correspondenceState = TestEntityDataBuilder.
                getCorrespondenceStateEntity(TestModelDataBuilder.REP_ID, "cds15");

        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.put(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(correspondenceState)));

        Assertions.assertThat(objectMapper.writeValueAsString(correspondenceState))
                .isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    void givenANewRepId_whenUpdateIsInvoked_thenCorrectResponseIsReturned() throws Exception {

        CorrespondenceStateEntity correspondenceState = TestEntityDataBuilder.
                getCorrespondenceStateEntity(TestModelDataBuilder.REP_ID + 1, "cds15");
        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.put(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(correspondenceState)));
        Assertions.assertThat(objectMapper.writeValueAsString(correspondenceState))
                .isEqualTo(result.getResponse().getContentAsString());
    }


    @Test
    void givenAValidRepId_whenGetStatusIsInvoked_thenContributionCountIsReturned() throws Exception {
        var response = runSuccessScenario(get(ENDPOINT_URL + "/repId/" + TestModelDataBuilder.REP_ID));
        Assertions.assertThat("appealCC")
                .isEqualTo(response.getResponse().getContentAsString());
    }

    @Test
    void givenAInvalidRepId_whenGetStatusIsInvoked_thenZeroIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("No corresponsdence state found for repId= " + INVALID_REP_ID,
                get(ENDPOINT_URL + "/repId/" + INVALID_REP_ID).contentType(MediaType.APPLICATION_JSON)));
    }
}
