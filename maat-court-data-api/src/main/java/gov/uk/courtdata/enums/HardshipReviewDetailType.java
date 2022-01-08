package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HardshipReviewDetailType {
    FUNDING("FUNDING", "Other Sources of Funding"),
    INCOME("INCOME", "Income Denied Access To"),
    EXPENDITURE("EXPENDITURE","Extra Expenditure"),
    SOL_COSTS("SOL COSTS","Solicitor Costs"),
    ACTION("ACTION","Review Progress")
    ;

    private String code;
    private String description;

    @JsonValue
    public String getCode() {
        return code;
    }

}
