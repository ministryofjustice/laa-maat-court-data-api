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
public enum HardshipReviewProgressResponse implements PersistableEnum<String> {

    FURTHER_RECEIVED("FURTHER RECEIVED", "Further Information received"),
    ORIGINAL_RECEIVED("ORIGINAL RECEIVED", "Original application received from HMCS"),
    ADDITIONAL_PROVIDED("ADDITIONAL PROVIDED", "Additional evidence provided");

    private String response;
    private String description;

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
