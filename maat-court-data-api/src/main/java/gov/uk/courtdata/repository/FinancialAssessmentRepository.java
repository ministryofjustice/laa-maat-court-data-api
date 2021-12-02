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
    void updateOldAssessments(@Param("repId") Integer repId);
}
