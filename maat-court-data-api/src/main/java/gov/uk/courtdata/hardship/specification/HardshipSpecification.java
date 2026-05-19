package gov.uk.courtdata.hardship.specification;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static gov.uk.courtdata.dto.application.AssessmentStatusDTO.INCOMPLETE;

import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.entity.HardshipReviewEntity_;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HardshipSpecification {

    public static Specification<HardshipReviewEntity> isReplaced() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(HardshipReviewEntity_.REPLACED), YES);
    }

    public static Specification<HardshipReviewEntity> isCurrent() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(HardshipReviewEntity_.REPLACED), NO);
    }

    public static Specification<HardshipReviewEntity> isInProgress() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(HardshipReviewEntity_.STATUS), INCOMPLETE);
    }

    public static Specification<HardshipReviewEntity> isValid() {
        return (root, query, criteriaBuilder) -> {
            Predicate isNull = criteriaBuilder.isNull(root.get(HardshipReviewEntity_.VALID));
            Predicate isFalse = criteriaBuilder.notEqual(root.get(HardshipReviewEntity_.VALID), NO);
            return criteriaBuilder.or(isNull, isFalse);
        };
    }

    public static Specification<HardshipReviewEntity> hasRepId(int repId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(HardshipReviewEntity_.REP_ID), repId);
    }
}
