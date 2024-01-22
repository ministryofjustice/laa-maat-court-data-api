package gov.uk.courtdata.dto;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorDTO {
    String code;
    String message;
}
