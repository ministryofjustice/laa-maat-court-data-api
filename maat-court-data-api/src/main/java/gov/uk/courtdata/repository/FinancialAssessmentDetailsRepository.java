package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentDetailEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialAssessmentDetailsRepository extends JpaRepository<FinancialAssessmentDetailEntity, Integer> {
    List<FinancialAssessmentDetailEntity> findAllByFinancialAssessmentId(Integer financialAssessmentId);
}
