package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HardshipReviewProgressResponse {
    FURTHER_RECEIVED("FURTHER RECEIVED", "Further Information received"),
    ORIGINAL_RECEIVED("ORIGINAL RECEIVED", "Original application received from HMCS"),
    ADDITIONAL_PROVIDED("ADDITIONAL PROVIDED","Additional evidence provided")
    ;

    private String code;
    private String description;

    @JsonValue
    public String getCode() {
        return code;
    }

}
