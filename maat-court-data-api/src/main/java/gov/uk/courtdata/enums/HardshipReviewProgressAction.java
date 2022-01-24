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
public enum HardshipReviewProgressAction implements PersistableEnum<String> {

    FURTHER_INFO("FURTHER INFO", "Further Information requested"),
    ORIG_APP_REQUESTED("ORIG APP REQUESTED", "Original Appliction Requested"),
    ORIG_APP_RETURNED("ORIG APP RETURNED", "Original Application returned to HMCS"),
    SOLICITOR_INFORMED("SOLICITOR INFORMED", "Solicitor Informed"),
    ADDITIONAL_EVIDENCE("ADDITIONAL EVIDENCE", "Additional Evidence Requested"),
    REJECTED_APP("REJECTED APP", "Rejected application"),
    OTHER("OTHER", "Other");

    private String code;
    private String description;

    @Override
    public String getValue() {
        return this.code;
    }

    @Converter(autoApply = true)
    private static class HardshipReviewProgressActionConverter extends AbstractEnumConverter<HardshipReviewProgressAction, String> {
        protected HardshipReviewProgressActionConverter() {
            super(HardshipReviewProgressAction.class);
        }
    }
}
