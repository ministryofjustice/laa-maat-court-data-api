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
public enum HardshipReviewProgressAction implements PersistableEnum<String> {

    FURTHER_INFO("FURTHER INFO", "Further Information requested"),
    ORIG_APP_REQUESTED("ORIG APP REQUESTED", "Original Application Requested"),
    ORIG_APP_RETURNED("ORIG APP RETURNED", "Original Application returned to HMCTS"),
    SOLICITOR_INFORMED("SOLICITOR INFORMED", "Solicitor Informed"),
    ADDITIONAL_EVIDENCE("ADDITIONAL EVIDENCE", "Additional Evidence Requested"),
    REJECTED_APP("REJECTED APP", "Rejected application"),
    OTHER("OTHER", "Other");

    @JsonValue
    private final String action;
    private final String description;

    @Override
    public String getValue() {
        return this.action;
    }

    @Converter(autoApply = true)
    private static class HardshipReviewProgressActionConverter extends AbstractEnumConverter<HardshipReviewProgressAction, String> {
        protected HardshipReviewProgressActionConverter() {
            super(HardshipReviewProgressAction.class);
        }
    }
}
