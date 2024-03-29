package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ChildWeightHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildWeightHistoryRepository extends JpaRepository<ChildWeightHistoryEntity, Integer> {
}