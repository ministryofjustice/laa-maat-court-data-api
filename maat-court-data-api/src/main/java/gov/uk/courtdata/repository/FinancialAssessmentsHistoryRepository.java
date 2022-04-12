package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialAssessmentsHistoryRepository extends JpaRepository<FinancialAssessmentsHistoryEntity, Integer> {
}