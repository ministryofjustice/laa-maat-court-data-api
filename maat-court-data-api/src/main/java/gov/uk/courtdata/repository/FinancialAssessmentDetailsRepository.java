package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialAssessmentDetailsRepository extends JpaRepository<FinancialAssessmentDetailEntity, Integer> {
    List<FinancialAssessmentDetailEntity> findAllByFinancialAssessmentId(Integer financialAssessmentId);
}
