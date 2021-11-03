package gov.uk.courtdata.model;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Unlink {

    private String defendantId;
    private Integer maatId;
    private String userId;
    private Integer reasonId;
    private String otherReasonText;
    private UUID laaTransactionId;
}
