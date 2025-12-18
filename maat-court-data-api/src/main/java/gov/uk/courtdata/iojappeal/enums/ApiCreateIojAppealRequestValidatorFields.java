package gov.uk.courtdata.iojappeal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiCreateIojAppealRequestValidatorFields {
    ERROR_FIELD_IS_MISSING("%s is missing."),
    ERROR_INCORRECT_COMBINATION("Incorrect Combination of Assessor and Reason."),
    ERROR_APPEAL_REASON_IS_INVALID("Appeal Reason Is Invalid."),

    REQUEST("Request"),

    METADATA("Metadata"),

    APPEAL("Appeal"),
    APPEAL_SUCCESSFUL("Appeal Successful"),
    APPLICATION_RECEIVED_DATE("Application Received Date"),
    LEGACY_APPLICATION_ID("Legacy Application Id"),
    APPEAL_REASON_HARDIOJ("HARDIOJ");

    private final String name;
}
