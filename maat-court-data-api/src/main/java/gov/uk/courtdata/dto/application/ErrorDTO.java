package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorDTO {
    String traceId;
    String code;
    String message;
}
