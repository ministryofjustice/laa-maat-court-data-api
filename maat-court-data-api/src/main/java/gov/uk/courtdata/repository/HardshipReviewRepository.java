package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HardshipReviewRepository extends JpaRepository<HardshipReviewEntity, Integer> {
    @Modifying
    @Query(value = "UPDATE HardshipReviewEntity hr set hr.replaced = 'Y' WHERE hr.repId = :repId and hr.financialAssessmentId <> :financialAssessmentId")
    void updateOldHardshipReviews(@Param("repId") Integer repId, @Param("financialAssessmentId") Integer financialAssessmentId);

    @Query(value = "SELECT hr FROM HardshipReviewEntity hr WHERE hr.repId = :repId AND hr.replaced = 'N'")
    HardshipReviewEntity findByRepId(@Param("repId") Integer repId);

    @Query(value = "SELECT hrd.* FROM TOGDATA.HARDSHIP_REVIEWS hr, TOGDATA.HARDSHIP_REVIEW_DETAILS hrd " +
            "WHERE HR.ID = HRD.HARD_ID and HRD.HRDT_TYPE = :detailType AND hr.replaced = 'N' AND HR.REP_ID = :repId", nativeQuery = true)
    List<HardshipReviewDetailEntity> findByDetailType(@Param("detailType") String detailType, @Param("repId") Integer repId);
}
