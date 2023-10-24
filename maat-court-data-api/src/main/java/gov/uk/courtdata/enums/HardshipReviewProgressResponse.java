package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import gov.uk.courtdata.converter.AbstractEnumConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Converter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum HardshipReviewProgressResponse implements PersistableEnum<String> {

    FURTHER_RECEIVED("FURTHER RECEIVED", "Further Information received"),
    ORIGINAL_RECEIVED("ORIGINAL RECEIVED", "Original application received from HMCS"),
    ADDITIONAL_PROVIDED("ADDITIONAL PROVIDED", "Additional evidence provided");

    @JsonValue
    private final String response;
    private final String description;

    @Override
    public String getValue() {
        return this.response;
    }

    @Converter(autoApply = true)
    private static class HardshipReviewProgressResponseConverter extends AbstractEnumConverter<HardshipReviewProgressResponse, String> {
        protected HardshipReviewProgressResponseConverter() {
            super(HardshipReviewProgressResponse.class);
        }
    }
}
