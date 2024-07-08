package gov.uk.courtdata.dces.request;

import gov.uk.courtdata.enums.FdcContributionsStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateFdcContributionRequest {
    private Integer fdcContributionId;
    private Integer repId;
    private FdcContributionsStatus previousStatus;
    private FdcContributionsStatus newStatus;
}
