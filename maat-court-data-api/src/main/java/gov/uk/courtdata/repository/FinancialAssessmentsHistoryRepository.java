package gov.uk.courtdata.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialAssessmentsHistoryRepository extends JpaRepository<FinancialAssessmentsHistory, Integer> {
}