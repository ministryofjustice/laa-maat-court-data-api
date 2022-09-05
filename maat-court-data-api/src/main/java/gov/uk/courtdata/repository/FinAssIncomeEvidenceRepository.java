package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinAssIncomeEvidenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinAssIncomeEvidenceRepository extends JpaRepository<FinAssIncomeEvidenceEntity, Integer> {

    @Query(value = "SELECT * FROM FIN_ASS_INCOME_EVIDENCE fa WHERE fa.FIAS_ID :repId AND fa.ACTIVE :active", nativeQuery = true)
    List<FinAssIncomeEvidenceEntity> findByFias_RepIdAndActiveEqualsIgnoreCase(Integer repId, String active);


}