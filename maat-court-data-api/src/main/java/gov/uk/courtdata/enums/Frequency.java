package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import gov.uk.courtdata.converter.AbstractEnumConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter
@AllArgsConstructor
public enum Frequency implements PersistableEnum<String> {

    WEEKLY("WEEKLY", "Weekly", 52),
    TWO_WEEKLY("2WEEKLY", "2 Weekly", 26),
    FOUR_WEEKLY("4WEEKLY", "4 Weekly", 13),
    MONTHLY("MONTHLY", "Monthly", 12),
    ANNUALLY("ANNUALLY", "Annually", 1);

    @JsonValue
    private String code;
    private String description;
    private int annualWeighting;

    @Override
    public String getValue() {
        return this.code;
    }

    @Converter(autoApply = true)
    private static class FrequencyConverter extends AbstractEnumConverter<Frequency, String> {
        protected FrequencyConverter() {
            super(Frequency.class);
        }
    }
}
