package gov.uk.courtdata.repository;

import gov.uk.courtdata.entity.HardshipReviewDetailReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HardshipReviewDetailReasonRepository extends JpaRepository<HardshipReviewDetailReasonEntity, Integer> {

    HardshipReviewDetailReasonEntity getByReasonIs(String reasonCode);

}
