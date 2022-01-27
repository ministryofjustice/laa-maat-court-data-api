package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.HardshipReviewProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HardshipReviewProgressRepository extends JpaRepository<HardshipReviewProgressEntity, Integer> {
    List<HardshipReviewProgressEntity> findByHardshipReviewId(Integer hardshipReviewId);
}
