package gov.uk.courtdata.integration.contribution;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
import uk.gov.justice.laa.crime.common.model.contribution.maat_api.CreateContributionRequest;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class ContributionsServiceIntegrationTest extends MockMvcIntegrationTest {

    private static final Integer INVALID_REP_ID = 9999;
    public Integer repId = 1234;

    @Autowired
    private ContributionsService contributionsService;

    private ContributionsEntity contributionsEntity;

    @BeforeEach
    void setUp() {

        RepOrderEntity repOrderEntity = repos.repOrder.saveAndFlush(TestEntityDataBuilder.getPopulatedRepOrder());
        repId = repOrderEntity.getId();

        ContributionsEntity contributions = TestEntityDataBuilder.getContributionsEntity();
        contributions.setRepOrder(repOrderEntity);

        ContributionFilesEntity contributionFilesEntity = TestEntityDataBuilder.getContributionFilesEntity();
        contributionFilesEntity.setFileId(contributions.getContributionFileId());
        repos.contributionFiles.saveAndFlush(contributionFilesEntity);

        contributionsEntity = repos.contributions.saveAndFlush(contributions);
    }

    @Test
    void givenValidRepId_WhenFindIsInvoked_thenCorrectResponseIsReturned() {
        List<ContributionsDTO> result = contributionsService.find(repId, true);
        assertThat(result).isNotEmpty();
        assertThat(result.getFirst().getId()).isGreaterThan(0);
        assertThat(result.getFirst().getRepId()).isEqualTo(repId);
    }

    @Test
    void givenInValidRepId_WhenFindIsInvoked_thenCorrectResponseIsReturned() {
        assertThatThrownBy(() -> contributionsService.find(INVALID_REP_ID, true))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Contributions entry not found for repId");
    }

    @Test
    void givenValidData_WhenCreateIsInvoked_thenCorrectResponseIsReturned() {
        CreateContributionRequest createContributions = TestModelDataBuilder.getCreateContributions(repId);
        ContributionsDTO result = contributionsService.create(createContributions);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
        assertThat(result.getMonthlyContributions()).isEqualTo(createContributions.getMonthlyContributions());
    }

    @Test
    void givenInValidData_WhenCreateIsInvoked_thenCorrectResponseIsReturned() {
        CreateContributionRequest createContributions = TestModelDataBuilder.getCreateContributions(repId);
        createContributions.setRepId(INVALID_REP_ID);
        assertThatThrownBy(() -> contributionsService.create(createContributions))
                .isInstanceOf(DataAccessException.class);
    }

    @Test
    @Disabled
    void givenValidRepId_whenGetContributionsSummaryIsInvoked_thenCorrectResponseIsReturned() {
        ContributionFilesEntity contributionFilesEntity = TestEntityDataBuilder.getContributionFilesEntity();
        contributionFilesEntity.setFileId(contributionsEntity.getContributionFileId());
        repos.contributionFiles.saveAndFlush(contributionFilesEntity);
        List<ContributionsSummaryDTO> contributionsSummary = contributionsService.getContributionsSummary(repId);
        assertThat(contributionsSummary).isNotEmpty();
    }

    @Test
    void givenInValidRepId_whenGetContributionsSummaryIsInvoked_thenCorrectResponseIsReturned() {
        assertThatThrownBy(() -> contributionsService.getContributionsSummary(INVALID_REP_ID))
                .isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("No contribution entries found for repId");
    }
}
