package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinAssIncomeEvidenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinAssIncomeEvidenceRepository extends JpaRepository<FinAssIncomeEvidenceEntity, Integer> {
    Integer deleteByFinancialAssessment_Id(Integer id);
}
