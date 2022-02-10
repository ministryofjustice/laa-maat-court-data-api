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
    @Query(value = "UPDATE PASSPORT_ASSESSMENTS pa set pa.REPLACED = 'Y' WHERE pa.REP_ID = :repId", nativeQuery = true)
    void updateAllPreviousPassportAssessmentsAsReplaced(@Param("repId") Integer repId);

    @Modifying
    @Query(value = "UPDATE PASSPORT_ASSESSMENTS pa set pa.REPLACED = 'Y' WHERE pa.ID <> :id AND pa.REP_ID = :repId", nativeQuery = true)
    void updatePreviousPassportAssessmentsAsReplaced(@Param("repId") Integer repId, @Param("id") Integer id);

    @Query(value = "SELECT count(*) FROM PASSPORT_ASSESSMENTS pa WHERE pa.repId = :repId AND pa.REPLACED = 'N' AND (pa.valid IS NOT NULL AND pa.valid <> 'N') AND pa.PAST_STATUS = 'IN PROGRESS'", nativeQuery = true)
    Long findOutstandingPassportAssessments(@Param("repId") Integer repId);
}
