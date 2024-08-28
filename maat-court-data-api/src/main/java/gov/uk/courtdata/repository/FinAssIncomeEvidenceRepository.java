package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinAssIncomeEvidenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FinAssIncomeEvidenceRepository extends JpaRepository<FinAssIncomeEvidenceEntity, Integer> {
    @Modifying
    @Query(value = "DELETE FROM TOGDATA.FIN_ASS_INCOME_EVIDENCE fiev where fiev.ID = :id", nativeQuery = true)
    void deleteByFinAssIncomeEvidenceId(@Param("id") Integer id);
}
