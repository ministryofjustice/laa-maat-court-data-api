package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentsHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialAssessmentsHistoryRepository extends JpaRepository<FinancialAssessmentsHistory, Integer> {
}