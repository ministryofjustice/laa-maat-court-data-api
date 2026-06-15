package gov.uk.courtdata.assessment.specification;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static gov.uk.courtdata.dto.application.AssessmentStatusDTO.INCOMPLETE;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity_;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.RepOrderEntity_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PassportAssessmentSpecification {

    public static Specification<PassportAssessmentEntity> hasRepId(int repId) {
        return (root, query, criteriaBuilder) -> {
            Join<PassportAssessmentEntity, RepOrderEntity> repOrder = root.join(PassportAssessmentEntity_.REP_ORDER);
            return criteriaBuilder.equal(repOrder.get(RepOrderEntity_.ID), repId);
        };
    }

    public static Specification<PassportAssessmentEntity> isCurrent() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(PassportAssessmentEntity_.REPLACED), NO);
    }

    public static Specification<PassportAssessmentEntity> isInProgress() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(PassportAssessmentEntity_.PAST_STATUS), INCOMPLETE);
    }

    public static Specification<PassportAssessmentEntity> isValid() {
        return (root, query, criteriaBuilder) -> {
            Predicate isNull = criteriaBuilder.isNull(root.get(PassportAssessmentEntity_.VALID));
            Predicate isYes = criteriaBuilder.equal(root.get(PassportAssessmentEntity_.VALID), YES);
            return criteriaBuilder.or(isNull, isYes);
        };
    }
}
