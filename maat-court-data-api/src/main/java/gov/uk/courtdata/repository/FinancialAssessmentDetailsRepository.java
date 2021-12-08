package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialAssessmentDetailsRepository extends JpaRepository<FinancialAssessmentDetailsEntity, Integer> {

}
