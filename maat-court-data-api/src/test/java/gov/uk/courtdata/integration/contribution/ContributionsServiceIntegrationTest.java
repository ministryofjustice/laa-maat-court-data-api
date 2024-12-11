package gov.uk.courtdata.integration.contribution;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.contribution.dto.ContributionsSummaryDTO;
import gov.uk.courtdata.contribution.service.ContributionsService;
import gov.uk.courtdata.dto.ContributionsDTO;
import gov.uk.courtdata.entity.ContributionFilesEntity;
import gov.uk.courtdata.entity.ContributionsEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import uk.gov.justice.laa.crime.common.model.contribution.maat_api.CreateContributionRequest;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class ContributionsServiceIntegrationTest extends MockMvcIntegrationTest {

    private static final Integer INVALID_REP_ID = 9999;
    public Integer REP_ID = 1234;

    @Autowired
    private ContributionsService contributionsService;

    private ContributionsEntity contributionsEntity;

    @BeforeEach
    public void setUp() {

        RepOrderEntity repOrderEntity = repos.repOrder.saveAndFlush(
            TestEntityDataBuilder.getPopulatedRepOrder());
        REP_ID = repOrderEntity.getId();

        ContributionsEntity contributions = TestEntityDataBuilder.getContributionsEntity();
        contributions.setRepOrder(repOrderEntity);

        ContributionFilesEntity contributionFilesEntity = TestEntityDataBuilder.getContributionFilesEntity();
        contributionFilesEntity.setFileId(contributions.getContributionFileId());
        repos.contributionFiles.saveAndFlush(contributionFilesEntity);

        contributionsEntity = repos.contributions.saveAndFlush(contributions);
    }

    @Test
    void givenValidRepId_WhenFindIsInvoked_thenCorrectResponseIsReturned() {
        List<ContributionsDTO> result = contributionsService.find(REP_ID, true);
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.get(0).getId()).isGreaterThan(0);
        assertThat(result.get(0).getRepId()).isEqualTo(REP_ID);
    }

    @Test
    void givenInValidRepId_WhenFindIsInvoked_thenCorrectResponseIsReturned() {
        assertThatThrownBy(() -> contributionsService.find(INVALID_REP_ID, true))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Contributions entry not found for repId");
    }

    @Test
    void givenValidData_WhenCreateIsInvoked_thenCorrectResponseIsReturned() {
        CreateContributionRequest createContributions = TestModelDataBuilder.getCreateContributions(REP_ID);
        ContributionsDTO result = contributionsService.create(createContributions);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
        assertThat(result.getMonthlyContributions()).isEqualTo(createContributions.getMonthlyContributions());
    }

    @Test
    void givenInValidData_WhenCreateIsInvoked_thenCorrectResponseIsReturned() {
        assertThatThrownBy(() -> {
            CreateContributionRequest createContributions = TestModelDataBuilder.getCreateContributions(REP_ID);
            createContributions.setRepId(INVALID_REP_ID);
            contributionsService.create(createContributions);
        }).isInstanceOf(DataAccessException.class);
    }

    @Test
    @Disabled
    void givenValidRepId_whenGetContributionsSummaryIsInvoked_thenCorrectResponseIsReturned() {
        ContributionFilesEntity contributionFilesEntity = TestEntityDataBuilder.getContributionFilesEntity();
        contributionFilesEntity.setFileId(contributionsEntity.getContributionFileId());
        repos.contributionFiles.saveAndFlush(contributionFilesEntity);
        List<ContributionsSummaryDTO> contributionsSummary = contributionsService.getContributionsSummary(REP_ID);
        assertThat(contributionsSummary.isEmpty()).isFalse();
    }

    @Test
    void givenInValidRepId_whenGetContributionsSummaryIsInvoked_thenCorrectResponseIsReturned() {
        assertThatThrownBy(() -> contributionsService.getContributionsSummary(INVALID_REP_ID))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("No contribution entries found for repId");
    }

}
