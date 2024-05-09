package gov.uk.courtdata.enums;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.MDC;

@Getter
@AllArgsConstructor
public enum LoggingData{

    LAA_TRANSACTION_ID("laaTransactionId"),
    MAATID("maatId"),
    CASE_URN("caseUrn"),
    MESSAGE("message"),
    REQUEST_TYPE("requestType");

    private final String mdcKey;

    public void putInMDC(Integer value) {
        MDC.put(mdcKey, String.valueOf(value));
    }

    public void putInMDC(UUID value) {
        MDC.put(mdcKey, value.toString());

    }
}