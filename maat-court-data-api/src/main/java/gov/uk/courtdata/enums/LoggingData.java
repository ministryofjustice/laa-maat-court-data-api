package gov.uk.courtdata.enums;

import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

@Getter
@AllArgsConstructor
public enum LoggingData {

  CASE_URN("caseUrn"),
  LAA_TRANSACTION_ID("laaTransactionId"),
  MAAT_ID("maatId"), // The domain concept of a MAAT Id is identical to a Rep Id.
  REQUEST_TYPE("requestType"),
  USN("usn");

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

  public void removeFromMdc(String key) {
    if (Objects.nonNull(key)) {
      MDC.remove(key);
    }
  }

  public String getValueFromMDC() {
    String valueFromMDC = MDC.get(key);

    return Optional.ofNullable(valueFromMDC)
        .orElse(StringUtils.EMPTY);
  }
}