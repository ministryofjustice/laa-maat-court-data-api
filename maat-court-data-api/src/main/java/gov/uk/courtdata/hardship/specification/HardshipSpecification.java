package gov.uk.courtdata.hardship.specification;

import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HardshipSpecification {

    public static Specification<HardshipReviewEntity> isReplaced() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(HardshipReviewEntity_.REPLACED), "Y");
    }

    public static Specification<HardshipReviewEntity> isCurrent() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(HardshipReviewEntity_.REPLACED), "N");
    }

    public static Specification<HardshipReviewEntity> hasRepId(int repId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(HardshipReviewEntity_.REP_ID), repId);
    }
}
