package gov.uk.courtdata.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HardshipReviewProgressAction {
    FURTHER_INFO("FURTHER INFO", "Further Information requested"),
    ORIG_APP_REQUESTED("ORIG APP REQUESTED", "Original Appliction Requested"),
    ORIG_APP_RETURNED("ORIG APP RETURNED","Original Application returned to HMCS"),
    SOLICITOR_INFORMED("SOLICITOR INFORMED","Solicitor Informed"),
    ADDITIONAL_EVIDENCE("ADDITIONAL EVIDENCE","Additional Evidence Requested"),
    REJECTED_APP("REJECTED APP","Rejected application"),
    OTHER("OTHER","Other")
    ;

    private String code;
    private String description;

    @JsonValue
    public String getCode() {
        return code;
    }

}
