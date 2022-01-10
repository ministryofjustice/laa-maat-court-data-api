package gov.uk.courtdata.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LoggingData{

    LAA_TRANSACTION_ID("laaTransactionId"),
    MAATID("maatId"),
    CASE_URN("caseUrn"),
    MESSAGE("message"),
    REQUEST_TYPE("requestType");

    private String value;
}