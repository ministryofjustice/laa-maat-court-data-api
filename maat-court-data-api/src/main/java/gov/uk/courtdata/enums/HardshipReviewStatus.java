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
public enum HardshipReviewStatus implements PersistableEnum<String> {

    IN_PROGRESS("IN PROGRESS", "Incomplete"),
    COMPLETE("COMPLETE", "Complete");

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
