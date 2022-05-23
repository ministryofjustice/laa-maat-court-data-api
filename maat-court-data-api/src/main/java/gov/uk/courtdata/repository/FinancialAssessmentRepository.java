package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinancialAssessmentRepository extends JpaRepository<FinancialAssessmentEntity, Integer> {

    @Modifying
    @Query(value = "UPDATE TOGDATA.FINANCIAL_ASSESSMENTS fa set fa.REPLACED = 'Y' WHERE fa.REP_ID = :repId", nativeQuery = true)
    void updateAllPreviousFinancialAssessmentsAsReplaced(@Param("repId") Integer repId);

    @Modifying
    @Query(value = "UPDATE TOGDATA.FINANCIAL_ASSESSMENTS fa set fa.REPLACED = 'Y' WHERE fa.ID <> :id AND fa.REP_ID = :repId", nativeQuery = true)
    void updatePreviousFinancialAssessmentsAsReplaced(@Param("repId") Integer repId, @Param("id") Integer id);

    @Query(value = "SELECT * FROM TOGDATA.FINANCIAL_ASSESSMENTS fa WHERE fa.REP_ID = :repId AND DATE_COMPLETED IS NOT NULL AND fa.REPLACED = 'N'", nativeQuery = true)
    Optional<FinancialAssessmentEntity> findCompletedAssessmentByRepId(@Param("repId") Integer repId);

    @Query(value = "SELECT count(*) FROM TOGDATA.FINANCIAL_ASSESSMENTS fa WHERE fa.REP_ID = :repId AND fa.REPLACED = 'N' AND (fa.VALID IS NULL OR fa.VALID <> 'N') AND (fa.FASS_FULL_STATUS = 'IN PROGRESS' OR fa.FASS_INIT_STATUS  = 'IN PROGRESS')", nativeQuery = true)
    Long findOutstandingFinancialAssessments(@Param("repId") Integer repId);
}
