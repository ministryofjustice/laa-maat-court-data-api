package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ChildWeightingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildWeightingsRepository extends JpaRepository<ChildWeightingsEntity, Integer> {

    List<ChildWeightingsEntity> findAllByFinancialAssessmentId(Integer financialAssessmentId);

}
