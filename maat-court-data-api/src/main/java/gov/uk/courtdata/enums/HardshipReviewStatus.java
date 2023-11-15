package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import gov.uk.courtdata.converter.AbstractEnumConverter;
import jakarta.persistence.Converter;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum HardshipReviewStatus implements PersistableEnum<String> {

    IN_PROGRESS("IN PROGRESS", "Incomplete"),
    COMPLETE("COMPLETE", "Complete");

    @JsonValue
    private String status;
    private String description;

    @Override
    public String getValue() {
        return this.status;
    }

    @Converter(autoApply = true)
    private static class HardshipReviewStatusConverter extends AbstractEnumConverter<HardshipReviewStatus, String> {
        protected HardshipReviewStatusConverter() {
            super(HardshipReviewStatus.class);
        }
    }
}
