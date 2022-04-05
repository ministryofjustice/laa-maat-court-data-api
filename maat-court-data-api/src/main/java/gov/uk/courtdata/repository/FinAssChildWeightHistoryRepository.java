package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinAssChildWeightHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinAssChildWeightHistoryRepository extends JpaRepository<FinAssChildWeightHistory, Integer> {
}