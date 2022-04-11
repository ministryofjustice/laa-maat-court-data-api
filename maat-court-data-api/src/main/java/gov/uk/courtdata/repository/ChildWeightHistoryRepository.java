package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ChildWeightHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildWeightHistoryRepository extends JpaRepository<ChildWeightHistoryEntity, Integer> {
    List<ChildWeightHistoryEntity> findAllByFinancialAssessmentId(Integer financialAssessmentId);
}