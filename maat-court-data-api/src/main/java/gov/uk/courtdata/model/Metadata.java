package gov.uk.courtdata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Metadata {

    private UUID laaTransactionId;
}
