package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.HardshipReviewProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HardshipReviewProgressRepository extends JpaRepository<HardshipReviewProgressEntity, Integer> {
    @Modifying
    @Query(value = "UPDATE TOGDATA.HARDSHIP_REVIEW_PROGRESS SET ACTIVE = null, REMOVED_DATE = :currDate WHERE HARE_ID = :hardshipReviewId AND ACTIVE = 'Y' AND nvl(DATE_MODIFIED, DATE_CREATED) < :currDate", nativeQuery = true)
    void updateHardshipReviewProgress(@Param("hardshipReviewId") Integer hardshipReviewId, @Param("currDate") LocalDateTime currDate);
}