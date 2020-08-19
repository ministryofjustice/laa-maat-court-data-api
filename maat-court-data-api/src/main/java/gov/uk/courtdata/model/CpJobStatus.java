package gov.uk.courtdata.model;


import gov.uk.courtdata.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CpJobStatus {

    private Integer laaStatusTransactionId;
    private UUID laaTransactionId;
    private JobStatus jobStatus;
    private Integer maatId;


}
