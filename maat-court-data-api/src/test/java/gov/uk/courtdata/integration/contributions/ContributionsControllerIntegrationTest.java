package gov.uk.courtdata.integration.contributions;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.entity.CorrespondenceEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import gov.uk.courtdata.repository.ContributionsRepository;
import gov.uk.courtdata.repository.CorrespondenceRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.MockMvcIntegrationTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ExtendWith(SoftAssertionsExtension.class)
@SpringBootTest(classes = {MAATCourtDataApplication.class, MockServicesConfig.class})
public class ContributionsControllerIntegrationTest extends MockMvcIntegrationTest {


    private static final Integer INVALID_REP_ID = 9999;
    private static final Integer INVALID_CONTRIBUTION_ID = 8888;
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/contributions";
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    ContributionsRepository contributionsRepository;

    CorrespondenceRepository correspondenceRepository;
    @Autowired
    MockMvc mvc;
    @Autowired
    private RepOrderRepository repOrderRepository;

    private ContributionsEntity contributionsEntity;

    @BeforeEach
    public void setUp(@Autowired RepOrderRepository repOrderRepository,
                      @Autowired ContributionsRepository contributionsRepository) {
        repOrderRepository.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder(TestEntityDataBuilder.REP_ID));
        CorrespondenceEntity correspondenceEntity = correspondenceRepository.saveAndFlush(TestEntityDataBuilder.getCorrespondenceEntity(1));
        ContributionsEntity contributions = TestEntityDataBuilder.getContributionsEntity();
        contributions.setcor
        contributionsEntity = contributionsRepository.saveAndFlush(contributions);
    }

    @AfterEach
    public void clearUp() {
        repOrderRepository.deleteAll();
        contributionsRepository.deleteAll();
    }

    @Test
    public void givenAEmptyContent_whenCreateIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenAInvalidRepId_whenCreateIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {
        CreateContributions createContributions = TestModelDataBuilder.getCreateContributions();
        createContributions.setRepId(INVALID_REP_ID);
        assertTrue(runBadRequestErrorScenario("MAAT/REP ID: " + INVALID_REP_ID + " is invalid.",
                post(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createContributions))));
    }

    @Test
    void givenAValidContent_whenCreateIsInvoked_theCorrectResponseIsReturned() throws Exception {

        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.post(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestModelDataBuilder.getCreateContributions())));

        ContributionsEntity currentEntity = objectMapper.readValue(result.getResponse().getContentAsString(), ContributionsEntity.class);
        ContributionsDTO expected = TestModelDataBuilder.getContributionsDTO();
        expected.setDateCreated(currentEntity.getDateCreated());
        expected.setDateModified(currentEntity.getDateModified());
        expected.setLatest(currentEntity.getLatest());
        expected.setId(currentEntity.getId());

        Assertions.assertThat(objectMapper.writeValueAsString(expected)).isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    void givenAEmptyContributionId_whenUpdateIsInvoked_theCorrectErrorResponseIsReturned() throws Exception {

        UpdateContributions updateContributions = TestModelDataBuilder.getUpdateContributions(INVALID_CONTRIBUTION_ID);
        assertTrue(runBadRequestErrorScenario("Contributions ID: " + INVALID_CONTRIBUTION_ID + " is invalid.",
                put(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateContributions))));
    }

    @Test
    void givenAValidContent_whenUpdateIsInvoked_theCorrectResponseIsReturned() throws Exception {

        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.put(ENDPOINT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(TestModelDataBuilder.getUpdateContributions(contributionsEntity.getId()))));
        contributionsEntity = contributionsRepository.findById(contributionsEntity.getId()).get();
        ContributionsEntity currentEntity = objectMapper.readValue(result.getResponse().getContentAsString(), ContributionsEntity.class);

        contributionsEntity.setContributionCap(currentEntity.getContributionCap());
        contributionsEntity.setMonthlyContributions(currentEntity.getMonthlyContributions());
        contributionsEntity.setUpfrontContributions(currentEntity.getUpfrontContributions());

        Assertions.assertThat(objectMapper.writeValueAsString(contributionsEntity))
                .isEqualTo(result.getResponse().getContentAsString());
    }

    @Test
    public void givenAInvalidContributionId_whenFindIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("Contributions entry not found for repId " + INVALID_REP_ID,
                get(ENDPOINT_URL + "/" + INVALID_REP_ID).contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    public void givenAValidParameter_whenFindIsInvoked_theCorrectResponseIsReturned() throws Exception {
        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID)
                .contentType(MediaType.APPLICATION_JSON));

        ContributionsEntity currentEntity = objectMapper.readValue(result.getResponse().getContentAsString(), ContributionsEntity.class);

        contributionsEntity.setContributionCap(currentEntity.getContributionCap());
        contributionsEntity.setMonthlyContributions(currentEntity.getMonthlyContributions());
        contributionsEntity.setUpfrontContributions(currentEntity.getUpfrontContributions());

        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(contributionsEntity));
    }

}
