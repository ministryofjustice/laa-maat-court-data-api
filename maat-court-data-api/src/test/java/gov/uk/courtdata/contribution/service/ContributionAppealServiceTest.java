package gov.uk.courtdata.contribution.service;

import gov.uk.courtdata.contribution.dto.ContributionAppealDTO;
import gov.uk.courtdata.repository.ContribAppealRulesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static gov.uk.courtdata.builder.TestModelDataBuilder.*;
import static java.util.Optional.of;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContributionAppealServiceTest {

    @InjectMocks
    private ContributionAppealService contributionAppealService;
    @Mock
    private ContribAppealRulesRepository contribAppealRulesRepository;

    @Test
    void givenContributionAppealDTO_whenGetContributionAmountIsInvoked_thenContributionAmountIsReturned() {
        ContributionAppealDTO contributionAppealDTO = new ContributionAppealDTO(CASE_TYPE, APTY_CODE, OUTCOME, ASSESSMENT_RESULT);
        when(contribAppealRulesRepository.findByCatyCaseTypeAndAptyCodeAndAndCcooOutcomeAndAssessmentResult(CASE_TYPE, APTY_CODE, OUTCOME, ASSESSMENT_RESULT))
                .thenReturn(of(() -> BigDecimal.valueOf(500.00)));
        BigDecimal contributionAmount = contributionAppealService.getContributionAmount(contributionAppealDTO);

        verify(contribAppealRulesRepository).findByCatyCaseTypeAndAptyCodeAndAndCcooOutcomeAndAssessmentResult(CASE_TYPE, APTY_CODE, OUTCOME, ASSESSMENT_RESULT);
        assertThat(contributionAmount).isEqualTo(CONTRIBUTION_AMOUNT);
    }

    @Test
    void givenContributionAppealDTOWithInvalidAptyCode_whenGetContributionAmountIsInvoked_thenNullIsReturned() {
        ContributionAppealDTO contributionAppealDTO = new ContributionAppealDTO(CASE_TYPE, "INVALID_APTY_CODE", OUTCOME, ASSESSMENT_RESULT);
        when(contribAppealRulesRepository
                .findByCatyCaseTypeAndAptyCodeAndAndCcooOutcomeAndAssessmentResult(CASE_TYPE, "INVALID_APTY_CODE", OUTCOME, ASSESSMENT_RESULT))
                .thenReturn(Optional.empty());

        BigDecimal contributionAmount = contributionAppealService.getContributionAmount(contributionAppealDTO);
        assertThat(contributionAmount).isNull();
        verify(contribAppealRulesRepository)
                .findByCatyCaseTypeAndAptyCodeAndAndCcooOutcomeAndAssessmentResult(CASE_TYPE, "INVALID_APTY_CODE", OUTCOME, ASSESSMENT_RESULT);

    }
}
