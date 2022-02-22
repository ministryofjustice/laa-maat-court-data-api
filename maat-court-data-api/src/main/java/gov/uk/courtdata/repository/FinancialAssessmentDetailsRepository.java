package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialAssessmentDetailsRepository extends JpaRepository<FinancialAssessmentDetailEntity, Integer> {
    List<FinancialAssessmentDetailEntity> findAllByFinancialAssessmentId(Integer financialAssessmentId);
}
