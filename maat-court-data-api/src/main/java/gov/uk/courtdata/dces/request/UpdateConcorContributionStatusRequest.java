package gov.uk.courtdata.dces.request;

import gov.uk.courtdata.enums.ConcorContributionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateConcorContributionStatusRequest {
    private ConcorContributionStatus status;
    private int recordCount;
}