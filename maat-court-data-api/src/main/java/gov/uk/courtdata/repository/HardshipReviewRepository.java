package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.HardshipReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HardshipReviewRepository extends JpaRepository<HardshipReviewEntity, Integer>, JpaSpecificationExecutor<HardshipReviewEntity> {

    @Modifying
    @Query(value = "UPDATE HardshipReviewEntity hr set hr.replaced = 'Y' WHERE hr.repId = :repId and hr.financialAssessmentId <> :financialAssessmentId")
    void replaceOldHardshipReviews(@Param("repId") Integer repId, @Param("financialAssessmentId") Integer financialAssessmentId);

    @Modifying
    @Query(value = "UPDATE HardshipReviewEntity hr set hr.replaced = 'Y' WHERE hr.repId = :repId and hr.courtType = :courtType")
    void replaceOldHardshipReviews(@Param("repId") Integer repId, @Param("courtType") String courtType);

    @Query(value = "SELECT hr FROM HardshipReviewEntity hr WHERE hr.repId = :repId AND hr.replaced = 'N'")
    HardshipReviewEntity findByRepId(@Param("repId") Integer repId);
}
