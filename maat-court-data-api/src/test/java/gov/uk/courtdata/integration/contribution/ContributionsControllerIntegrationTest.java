package gov.uk.courtdata.integration.contribution;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.entity.CorrespondenceEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.common.model.contribution.maat_api.CreateContributionRequest;

@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class})
class ContributionsControllerIntegrationTest extends MockMvcIntegrationTest {


    private static final Integer INVALID_REP_ID = 9999;
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/contributions";
    public Integer REP_ID = 1234;
    @Autowired
    protected ObjectMapper objectMapper;

    private ContributionsEntity contributionsEntity;

    @BeforeEach
    public void setUp() {
        RepOrderEntity repOrderEntity = repos.repOrder.saveAndFlush(
            TestEntityDataBuilder.getPopulatedRepOrder());
        REP_ID = repOrderEntity.getId();

        ContributionFilesEntity contributionFilesEntity = repos.contributionFiles.saveAndFlush(
            TestEntityDataBuilder.getContributionFilesEntity());
        CorrespondenceEntity correspondence = TestEntityDataBuilder.getCorrespondenceEntity(1);
        correspondence.setRepId(REP_ID);
        CorrespondenceEntity correspondenceEntity = repos.correspondence.saveAndFlush(
            correspondence);

        ContributionsEntity contributions = TestEntityDataBuilder.getContributionsEntity();
        contributions.setCorrespondenceId(correspondenceEntity.getId());
        contributions.setContributionFileId(contributionFilesEntity.getFileId());
        contributions.setRepOrder(repOrderEntity);
        contributionsEntity = repos.contributions.saveAndFlush(contributions);

        RepOrderEntity repOrder = repos.repOrder.saveAndFlush(
            TestEntityDataBuilder.getPopulatedRepOrder());
        ContributionsEntity contributionsEntity = TestEntityDataBuilder.getContributionsEntity();
        contributionsEntity.setRepOrder(repOrder);
        repos.contributions.saveAndFlush(contributionsEntity);
    }

    @AfterEach
    public void clearUp() {
        contributionsEntity = null;
    }

    @Test
    void givenAEmptyContent_whenCreateIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAInvalidRepId_whenCreateIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        CreateContributionRequest createContributions = TestModelDataBuilder.getCreateContributions(REP_ID);
        createContributions.setRepId(INVALID_REP_ID);
        assertTrue(runBadRequestErrorScenario("MAAT/REP ID [" + INVALID_REP_ID + "] is invalid",
                post(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createContributions))));
    }

    @Test
    void givenAValidContent_whenCreateIsInvoked_theCorrectResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getCreateContributions(REP_ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.repId").value(REP_ID));
    }

    @Test
    void givenAInvalidContributionId_whenFindIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("Contributions entry not found for repId " + INVALID_REP_ID,
                get(ENDPOINT_URL + "/" + INVALID_REP_ID).contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    void givenAValidParameter_whenFindIsInvoked_theCorrectResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].repId").value(REP_ID));
    }

    @Test
    void givenAValidRepIdAndLatestContributionAsTrue_whenFindIsInvoked_theCorrectResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + REP_ID
                        + "?findLatestContribution=true").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].repId").value(REP_ID));
    }

    @Test
    void givenValidRepId_whenGetContributionsSummaryIsInvoked_thenCorrectResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + REP_ID
                + "/summary").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(contributionsEntity.getId()))
            .andExpect(jsonPath("$[0].basedOn").value("Means"))
            .andExpect(jsonPath("$[0].upliftApplied").value("Y"));
    }

    @Test
    void givenRepIdWithNoContributions_whenGetContributionsSummaryIsInvoked_thenNotFoundResponseIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario(String.format("No contribution entries found for repId: %d", INVALID_REP_ID),
                get(ENDPOINT_URL + "/" + INVALID_REP_ID + "/summary").contentType(MediaType.APPLICATION_JSON)));
    }
}
