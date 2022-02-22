package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.HardshipReviewDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HardshipReviewDetailRepository extends JpaRepository<HardshipReviewDetailEntity, Integer> {
    List<HardshipReviewDetailEntity> findAllByHardshipReviewId(Integer hardshipReviewId);
}
