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
    @Query(value = "UPDATE PassportAssessmentEntity pa set pa.replaced = 'Y' WHERE pa.repId = :repId")
    void updateAllPreviousPassportAssessmentsAsReplaced(@Param("repId") Integer repId);

    @Modifying
    @Query(value = "UPDATE PassportAssessmentEntity pa set pa.replaced = 'Y' WHERE pa.id <> :id AND pa.repId = :repId")
    void updatePreviousPassportAssessmentsAsReplaced(@Param("repId") Integer repId, @Param("id") Integer id);
}
