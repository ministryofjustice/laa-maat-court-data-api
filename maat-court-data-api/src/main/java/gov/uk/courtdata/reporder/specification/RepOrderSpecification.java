package gov.uk.courtdata.reporder.specification;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.entity.RepOrderEntity_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RepOrderSpecification {

    public static Specification<RepOrderEntity> hasId(int id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(RepOrderEntity_.ID), id);
    }

    public static Specification<RepOrderEntity> hasSentenceOrderDate() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNotNull(root.get(RepOrderEntity_.SENTENCE_ORDER_DATE));
    }
}
