package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HardshipReviewDetailRepository extends JpaRepository<HardshipReviewDetailEntity, Integer> {
    List<HardshipReviewDetailEntity> findAllByHardshipReviewId(Integer hardshipReviewId);

    @Modifying
    @Query(value = "UPDATE TOGDATA.HARDSHIP_REVIEW_DETAILS SET ACTIVE = null, REMOVED_DATE = :currDate WHERE HARD_ID = :hardshipReviewId AND ACTIVE = 'Y' AND nvl(DATE_MODIFIED, DATE_CREATED) < :currDate", nativeQuery = true)
    void archive(@Param("hardshipReviewId") Integer hardshipReviewId, @Param("currDate") LocalDateTime currDate);
}
