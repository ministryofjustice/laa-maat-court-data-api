package gov.uk.courtdata.dces.request;

//similar class exist as FdcContributionDTO.

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFdcContributionRequest {
    private int fdcId;
    private Boolean lgfsComplete;
    private Boolean agfsComplete;
    private Boolean manualAcceleration;
    private String status;
}
