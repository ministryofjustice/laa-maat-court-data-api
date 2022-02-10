package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialAssessmentRepository extends JpaRepository<FinancialAssessmentEntity, Integer> {

    List<FinancialAssessmentEntity> findByRepId(Integer repId);

    @Modifying
    @Query(value = "UPDATE FinancialAssessmentEntity fa set fa.replaced = 'Y' WHERE fa.repId = :repId")
    void updateAllPreviousFinancialAssessmentsAsReplaced(@Param("repId") Integer repId);

    @Modifying
    @Query(value = "UPDATE FinancialAssessmentEntity fa set fa.replaced = 'Y' WHERE fa.id <> :id AND fa.repId = :repId")
    void updatePreviousFinancialAssessmentsAsReplaced(@Param("repId") Integer repId, @Param("id") Integer id);

    @Query(value = "SELECT count(*) FROM FINANCIAL_ASSESSMENTS fa WHERE fa.repId = :repId AND fa.REPLACED = 'N' AND (fa.valid IS NOT NULL AND fa.valid <> 'N') AND (fa.FASS_FULL_STATUS = 'IN PROGRESS' OR fa.FASS_INIT_STATUS  = 'IN PROGRESS')", nativeQuery = true)
    Long findOutstandingFinancialAssessments(@Param("repId") Integer repId);

}
