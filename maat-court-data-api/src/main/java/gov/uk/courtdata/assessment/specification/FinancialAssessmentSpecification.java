package gov.uk.courtdata.assessment.specification;

import static gov.uk.courtdata.constants.CourtDataConstants.NO;
import static gov.uk.courtdata.constants.CourtDataConstants.YES;
import static gov.uk.courtdata.dto.application.AssessmentStatusDTO.INCOMPLETE;

import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.FinancialAssessmentEntity_;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.RepOrderEntity_;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FinancialAssessmentSpecification {

    public static Specification<FinancialAssessmentEntity> isCurrent() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(FinancialAssessmentEntity_.REPLACED), NO);
    }

    public static Specification<FinancialAssessmentEntity> hasRepId(int repId) {
        return (root, query, criteriaBuilder) -> {
            Join<FinancialAssessmentEntity, RepOrderEntity> repOrder = root.join(FinancialAssessmentEntity_.REP_ORDER);
            return criteriaBuilder.equal(repOrder.get(RepOrderEntity_.ID), repId);
        };
    }

    public static Specification<FinancialAssessmentEntity> isInProgress() {
        return (root, query, criteriaBuilder) -> {
            Predicate fullStatus =
                    criteriaBuilder.equal(root.get(FinancialAssessmentEntity_.FASS_FULL_STATUS), INCOMPLETE);
            Predicate initStatus =
                    criteriaBuilder.equal(root.get(FinancialAssessmentEntity_.FASS_INIT_STATUS), INCOMPLETE);
            return criteriaBuilder.or(fullStatus, initStatus);
        };
    }

    public static Specification<FinancialAssessmentEntity> isValid() {
        return (root, query, criteriaBuilder) -> {
            Predicate isNull = criteriaBuilder.isNull(root.get(FinancialAssessmentEntity_.VALID));
            Predicate isYes = criteriaBuilder.equal(root.get(FinancialAssessmentEntity_.VALID), YES);
            return criteriaBuilder.or(isNull, isYes);
        };
    }
}
