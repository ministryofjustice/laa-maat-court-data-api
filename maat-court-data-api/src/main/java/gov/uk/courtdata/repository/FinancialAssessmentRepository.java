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
    @Query(value = "UPDATE FINANCIAL_ASSESSMENTS fa set fa.REPLACED = 'Y' WHERE fa.REP_ID = :repId", nativeQuery = true)
    void updateOldAssessments(@Param("repId") Integer repId);

    @Query(value = "SELECT * FROM FINANCIAL_ASSESSMENTS fa WHERE fa.REP_ID = :repId AND DATE_COMPLETED IS NOT NULL AND fa.REPLACED = 'N'", nativeQuery = true)
    Optional<FinancialAssessmentEntity> findCompletedAssessmentByRepId(@Param("repId") Integer repId);
}
