package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import gov.uk.courtdata.converter.AbstractEnumConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import jakarta.persistence.Converter;


@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum HardshipReviewDetailType implements PersistableEnum<String> {

    FUNDING("FUNDING", "Other Sources of Funding"),
    INCOME("INCOME", "Income Denied Access To"),
    EXPENDITURE("EXPENDITURE", "Extra Expenditure"),
    SOL_COSTS("SOL COSTS", "Solicitor Costs"),
    ACTION("ACTION", "Review Progress");

    @JsonValue
    private final String type;
    private final String description;

    @Override
    public String getValue() {
        return this.type;
    }

    @Converter(autoApply = true)
    private static class HardshipReviewDetailTypeConverter extends AbstractEnumConverter<HardshipReviewDetailType, String> {
        protected HardshipReviewDetailTypeConverter() {
            super(HardshipReviewDetailType.class);
        }
    }
}
