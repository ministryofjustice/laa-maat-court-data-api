package gov.uk.courtdata.contribution.service;

import gov.uk.courtdata.contribution.dto.ContributionAppealDTO;
import gov.uk.courtdata.contribution.projection.ContributionAmountView;
import gov.uk.courtdata.repository.ContribAppealRulesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContributionAppealService {

    private final ContribAppealRulesRepository contribAppealRulesRepository;

    @Transactional(readOnly = true)
    public BigDecimal getContributionAmount(final ContributionAppealDTO contribAppealDTO) {
        log.info("Get contribution amount for {}", contribAppealDTO);
        Optional<ContributionAmountView> result = contribAppealRulesRepository
                .findByCatyCaseTypeAndAptyCodeAndAndCcooOutcomeAndAssessmentResult(contribAppealDTO.getCaseType(),
                                                                                   contribAppealDTO.getAppealType(),
                                                                                   contribAppealDTO.getOutcome(),
                                                                                   contribAppealDTO.getAssessmentResult()
                );

        if (result.isPresent()) {
            BigDecimal contributionAmount = result.get().getContribAmount();
            log.info(
                    "Contribution amount= {} found for for caty_case_type={}, apty_code={}, ccoo_outcome={}, assessment_result={}",
                    contributionAmount,
                    contribAppealDTO.getCaseType(),
                    contribAppealDTO.getAppealType(),
                    contribAppealDTO.getOutcome(),
                    contribAppealDTO.getAssessmentResult()
            );
            return contributionAmount;
        } else {
            return null;
        }
    }
}
