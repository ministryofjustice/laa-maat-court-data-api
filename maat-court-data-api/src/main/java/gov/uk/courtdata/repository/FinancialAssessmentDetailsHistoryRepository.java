package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentDetailsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialAssessmentDetailsHistoryRepository extends JpaRepository<FinancialAssessmentDetailsHistoryEntity, Integer> {
}