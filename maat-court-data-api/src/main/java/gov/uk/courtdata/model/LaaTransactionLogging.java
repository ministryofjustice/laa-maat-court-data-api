package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.UUID;

@Builder
@AllArgsConstructor
@ToString
@Value
public class LaaTransactionLogging {

    private UUID laaTransactionId;
    private Integer maatId;
    private String caseUrn;
    private String message;

}
