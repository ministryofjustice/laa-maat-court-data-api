package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import gov.uk.courtdata.helper.AbstractEnumConverter;
import gov.uk.courtdata.helper.PersistableEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Converter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum HardshipReviewDetailType implements PersistableEnum<String> {

    FUNDING("FUNDING", "Other Sources of Funding"),
    INCOME("INCOME", "Income Denied Access To"),
    EXPENDITURE("EXPENDITURE", "Extra Expenditure"),
    SOL_COSTS("SOL COSTS", "Solicitor Costs"),
    ACTION("ACTION", "Review Progress");

    private String type;
    private String description;

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