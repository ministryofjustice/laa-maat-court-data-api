package gov.uk.courtdata.enums;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.MDC;

@Getter
@AllArgsConstructor
public enum LoggingData {

  LAA_TRANSACTION_ID("laaTransactionId"),
  MAATID("maatId"),
  CASE_URN("caseUrn"),
  MESSAGE("message"),
  REQUEST_TYPE("requestType");

  private final String key;

  public void putInMDC(Integer value) {
    if (Objects.nonNull(value)) {
      putInMDC(String.valueOf(value));
    }
  }

  public void putInMDC(String value) {
    if (Objects.nonNull(value)) {
      MDC.put(key, value);
    }
  }
}