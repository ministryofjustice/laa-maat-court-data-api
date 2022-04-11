package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentDetailsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialAssessmentDetailsHistoryRepository extends JpaRepository<FinancialAssessmentDetailsHistoryEntity, Integer> {
    List<FinancialAssessmentDetailsHistoryEntity> findAllByFinancialAssessmentId(Integer financialAssessmentId);
}