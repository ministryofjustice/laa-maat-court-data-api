package gov.uk.courtdata.integration.contribution;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contribution.model.CreateContributions;
import gov.uk.courtdata.contribution.model.UpdateContributions;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.entity.CorrespondenceEntity;
import gov.uk.courtdata.integration.MockServicesConfig;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

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

    @Autowired
    CorrespondenceRepository correspondenceRepository;
    @Autowired
    MockMvc mvc;
    @Autowired
    private RepOrderRepository repOrderRepository;

    private ContributionsEntity contributionsEntity;

    @BeforeEach
    public void setUp() {
        repOrderRepository.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder(TestEntityDataBuilder.REP_ID));
        CorrespondenceEntity correspondenceEntity = correspondenceRepository.saveAndFlush(TestEntityDataBuilder.getCorrespondenceEntity(1));
        ContributionsEntity contributions = TestEntityDataBuilder.getContributionsEntity();
        contributions.setCorrespondenceId(correspondenceEntity.getId());
        contributionsEntity = contributionsRepository.saveAndFlush(contributions);

        ContributionsEntity conEntity = TestEntityDataBuilder.getContributionsEntity();
        conEntity.setLatest(false);
        contributions.setCorrespondenceId(correspondenceEntity.getId());
        contributionsRepository.saveAndFlush(conEntity);



        repOrderRepository.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder(TestEntityDataBuilder.REP_ID + 1));
        ContributionsEntity contributionsEntity = TestEntityDataBuilder.getContributionsEntity();
        contributionsEntity.setRepId(TestEntityDataBuilder.REP_ID + 1);
        contributionsRepository.saveAndFlush(contributionsEntity);
    }

    @AfterEach
    public void clearUp() {
        contributionsEntity = null;
        contributionsRepository.deleteAll();
        repOrderRepository.deleteAll();
    }

    @Test
    void givenAEmptyContent_whenCreateIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
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
    void givenAInvalidContributionId_whenFindIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("Contributions entry not found for repId " + INVALID_REP_ID,
                get(ENDPOINT_URL + "/" + INVALID_REP_ID).contentType(MediaType.APPLICATION_JSON)));
    }

    @Test
    void givenAValidParameter_whenFindIsInvoked_theCorrectResponseIsReturned() throws Exception {
        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID)
                .contentType(MediaType.APPLICATION_JSON));
        List<ContributionsEntity> contributionsEntityList = contributionsRepository.findAllByRepId(TestModelDataBuilder.REP_ID);
        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(contributionsEntityList));
    }

    @Test
    void givenAValidRepIdAndLatestContributionAsTrue_whenFindIsInvoked_theCorrectResponseIsReturned() throws Exception {
        MvcResult result = runSuccessScenario(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID
                + "?findLatestContribution=true").contentType(MediaType.APPLICATION_JSON));
        ContributionsEntity contributionsEntityList = contributionsRepository.findByRepIdAndLatestIsTrue(TestModelDataBuilder.REP_ID);
        Assertions.assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(List.of(contributionsEntityList)));
    }

    @Test
    void givenAValidRepId_whenGetContributionCountIsInvoked_thenContributionCountIsReturned() throws Exception {
        var response = runSuccessScenario(head(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "/contribution"));
        Assertions.assertThat(response.getResponse().getHeader(HttpHeaders.CONTENT_LENGTH)).isEqualTo("1");
    }

    @Test
    void givenAValidRepIdAndEmptyCorrespondence_whenGetContributionCountIsInvoked_thenZeroIsReturned() throws Exception {
        assertTrue(runNotFoundErrorScenario("Contributions entry not found for repId " + INVALID_REP_ID,
                get(ENDPOINT_URL + "/" + INVALID_REP_ID).contentType(MediaType.APPLICATION_JSON)));
    }
}
