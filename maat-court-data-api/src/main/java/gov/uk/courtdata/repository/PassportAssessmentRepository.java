package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PassportAssessmentRepository extends JpaRepository<PassportAssessmentEntity, Integer> {

    @Modifying
    @Query(value = "UPDATE TOGDATA.PASSPORT_ASSESSMENTS pa set pa.REPLACED = 'Y' WHERE pa.REP_ID = :repId", nativeQuery = true)
    void updateAllPreviousPassportAssessmentsAsReplaced(@Param("repId") Integer repId);

    @Modifying
    @Query(value = "UPDATE TOGDATA.PASSPORT_ASSESSMENTS pa set pa.REPLACED = 'Y' WHERE pa.ID <> :id AND pa.REP_ID = :repId", nativeQuery = true)
    void updatePreviousPassportAssessmentsAsReplaced(@Param("repId") Integer repId, @Param("id") Integer id);

    @Query(value = "SELECT count(*) FROM TOGDATA.PASSPORT_ASSESSMENTS pa WHERE pa.REP_ID = :repId AND pa.REPLACED = 'N' AND (pa.VALID IS NULL OR pa.VALID <> 'N') AND pa.PAST_STATUS = 'IN PROGRESS'", nativeQuery = true)
    Long findOutstandingPassportAssessments(@Param("repId") Integer repId);

    @Query(value = "SELECT pa FROM PassportAssessmentEntity pa WHERE pa.repId = :repId AND pa.replaced = 'N'")
    PassportAssessmentEntity findByRepId(@Param("repId") Integer repId);
}
