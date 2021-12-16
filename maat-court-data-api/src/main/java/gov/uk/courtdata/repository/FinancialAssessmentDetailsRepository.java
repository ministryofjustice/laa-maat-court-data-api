package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialAssessmentDetailsRepository extends JpaRepository<FinancialAssessmentDetailsEntity, Integer> {
    List<FinancialAssessmentDetailsEntity> findAllByFinancialAssessmentId(Integer financialAssessmentId);
}
