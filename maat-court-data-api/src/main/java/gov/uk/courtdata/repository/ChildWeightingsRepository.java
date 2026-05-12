package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.ChildWeightingsEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildWeightingsRepository extends JpaRepository<ChildWeightingsEntity, Integer> {

    List<ChildWeightingsEntity> findAllByFinancialAssessmentId(Integer financialAssessmentId);
}
