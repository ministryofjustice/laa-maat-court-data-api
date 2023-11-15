package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum HardshipReviewDetailReason {

    EVIDENCE_SUPPLIED("Evidence Supplied"),
    ALLOWABLE_EXPENSE("Allowable Expense"),
    ESSENTIAL_NEED_FOR_WORK("Essential - need for work"),
    ESSENTIAL_ITEM("Essential Item"),
    ARRANGEMENT_IN_PLACE("Arrangement in place"),
    NO_EVIDENCE_SUPPLIED("No evidence supplied"),
    INSUFFICIENT_EVIDENCE_SUPPLIED("Insufficient evidence supplied"),
    NON_ESSENTIAL_ITEM_EXPENSE("Non-essential item/expense"),
    COVERED_BY_LIVING_EXPENSE("Covered by living expense"),
    NOT_ALLOWABLE_DIFF_FROM_NON_ESSENTIAL("Not allowable (diff from non-essential)"),
    NOT_IN_COMPUTATION_PERIOD("Not in computation period");

    @JsonValue
    @JsonPropertyDescription("Hardship review detail reasons")
    private final String reason;

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
}
