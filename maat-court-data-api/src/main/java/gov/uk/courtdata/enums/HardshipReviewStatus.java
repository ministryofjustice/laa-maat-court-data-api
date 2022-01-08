package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HardshipReviewStatus {
    IN_PROGRESS("IN PROGRESS", "Incomplete"),
    COMPLETE("COMPLETE", "Complete")
    ;

    private String status;
    private String description;

    @JsonValue
    public String getStatus() {
        return status;
    }

}
