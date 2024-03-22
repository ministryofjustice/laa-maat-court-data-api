package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import gov.uk.courtdata.converter.AbstractEnumConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum HardshipReviewDetailReason implements PersistableEnum<Integer> {

    EVIDENCE_SUPPLIED("Evidence Supplied", 7),
    ALLOWABLE_EXPENSE("Allowable Expense", 8),
    ESSENTIAL_NEED_FOR_WORK("Essential - need for work", 9),
    ESSENTIAL_ITEM("Essential Item", 10),
    ARRANGEMENT_IN_PLACE("Arrangement in place", 11),
    NO_EVIDENCE_SUPPLIED("No evidence supplied", 11010331),
    INSUFFICIENT_EVIDENCE_SUPPLIED("Insufficient evidence supplied", 11010332),
    NON_ESSENTIAL_ITEM_EXPENSE("Non-essential item/expense", 11010333),
    COVERED_BY_LIVING_EXPENSE("Covered by living expense", 11010334),
    NOT_ALLOWABLE_DIFF_FROM_NON_ESSENTIAL("Not allowable (diff from non-essential)", 11010335),
    NOT_IN_COMPUTATION_PERIOD("Not in computation period", 11010336);

    @JsonValue
    @JsonPropertyDescription("Hardship review detail reasons")
    private final String reason;
    private final int id;

    public static HardshipReviewDetailReason getFrom(String reason) {
        if (StringUtils.isBlank(reason)) {
            return null;
        }

        return Stream.of(HardshipReviewDetailReason.values())
                .filter(hardshipReviewDetailReason -> hardshipReviewDetailReason.reason.equals(reason))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Hardship review detail reason: %s does not exist.", reason)));
    }

    @Override
    public Integer getValue() {
        return this.id;
    }

    @Converter(autoApply = true)
    private static class HardshipReviewDetailReasonConverter extends AbstractEnumConverter<HardshipReviewDetailReason, Integer> {
        protected HardshipReviewDetailReasonConverter() {
            super(HardshipReviewDetailReason.class);
        }
    }
}
