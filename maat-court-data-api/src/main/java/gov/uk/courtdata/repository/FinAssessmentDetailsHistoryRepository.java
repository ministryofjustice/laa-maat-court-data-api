package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinAssessmentDetailsHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinAssessmentDetailsHistoryRepository extends JpaRepository<FinAssessmentDetailsHistory, Integer> {
}