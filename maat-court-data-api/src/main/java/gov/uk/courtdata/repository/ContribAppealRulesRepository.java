package gov.uk.courtdata.repository;

import gov.uk.courtdata.contribution.projection.ContributionAmountView;
import gov.uk.courtdata.entity.ContribAppealRulesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContribAppealRulesRepository extends JpaRepository<ContribAppealRulesEntity, Integer> {

    Optional<ContributionAmountView> findByCatyCaseTypeAndAptyCodeAndAndCcooOutcomeAndAssessmentResult(String caseType,
                                                                                                       String appealType,
                                                                                                       String outcome,
                                                                                                       String assessmentResult);
}