package gov.uk.courtdata.integration.correspondence;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.enums.contribution.CorrespondenceStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
class CorrespondenceStateControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final Integer NEW_REP_ID = 123;
    private static final Integer EXISTING_REP_ID = 456;
    private static final Integer INVALID_REP_ID = 9999;
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/{repId}/correspondence-state";

    @Autowired
    MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() {
        repos.correspondenceState.saveAndFlush(
                TestEntityDataBuilder.getCorrespondenceStateEntity(
                        EXISTING_REP_ID, CorrespondenceStatus.APPEAL_CC
                )
        );
    }

    @Test
    void givenAEmptyContent_whenCreateIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL, EXISTING_REP_ID).content("{}")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAValidContent_whenCreateIsInvoked_thenCorrectResponseIsReturned() throws Exception {
        runSuccessScenario(
                MockMvcRequestBuilders.post(ENDPOINT_URL, NEW_REP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CorrespondenceStatus.NONE))
        );
        CorrespondenceStateEntity entity = repos.correspondenceState.findByRepId(NEW_REP_ID);
        assertThat(CorrespondenceStatus.NONE.getStatus())
                .isEqualTo(entity.getStatus());
    }

    @Test
    void givenAEmptyContent_whenUpdateIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL, EXISTING_REP_ID).content("{}")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAnExistingRepId_whenUpdateIsInvoked_thenCorrespondenceStatusIsUpdated() throws Exception {
        runSuccessScenario(
                MockMvcRequestBuilders.put(ENDPOINT_URL, EXISTING_REP_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(CorrespondenceStatus.CDS15))
        );
        CorrespondenceStateEntity entity = repos.correspondenceState.findByRepId(EXISTING_REP_ID);
        assertThat(CorrespondenceStatus.CDS15.getStatus())
                .isEqualTo(entity.getStatus());
    }

    @Test
    void givenAnInvalidRepId_whenUpdateIsInvoked_thenErrorIsReturned() throws Exception {
        String errorMessage = "No correspondence state found for repId= " + INVALID_REP_ID;
        assertTrue(
                runNotFoundErrorScenario(
                        errorMessage,
                        MockMvcRequestBuilders.put(ENDPOINT_URL, INVALID_REP_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(CorrespondenceStatus.CDS15))
                )
        );
    }

    @Test
    void givenAnExistingRepId_whenFindIsInvoked_thenCorrespondenceStatusIsReturned() throws Exception {
        assertTrue(runSuccessScenario(CorrespondenceStatus.APPEAL_CC, get(ENDPOINT_URL, EXISTING_REP_ID)));
    }

    @Test
    void givenAnInvalidRepId_whenFindIsInvoked_thenErrorIsReturned() throws Exception {
        String errorMessage = "No correspondence state found for repId= " + INVALID_REP_ID;
        assertTrue(
                runNotFoundErrorScenario(
                        errorMessage,
                        get(ENDPOINT_URL, INVALID_REP_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                )
        );
    }
}
