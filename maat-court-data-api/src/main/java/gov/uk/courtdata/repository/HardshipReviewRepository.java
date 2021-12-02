package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.HardshipReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HardshipReviewRepository extends JpaRepository<HardshipReviewEntity, Integer> {
    @Modifying
    @Query(value = "UPDATE HardshipReviewEntity hr set hr.replaced = 'Y' WHERE hr.repId = :repId and hr.fiasId <> :financialAssessmentId")
    void updateOldHardshipReviews(@Param("repId") Integer repId, @Param("financialAssessmentId") Integer financialAssessmentId);
}
