package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentDetailsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialAssessmentDetailsHistoryRepository extends JpaRepository<FinancialAssessmentDetailsHistoryEntity, Integer> {
}