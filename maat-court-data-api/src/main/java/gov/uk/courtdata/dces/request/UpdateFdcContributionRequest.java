package gov.uk.courtdata.dces.request;

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
    private Integer fdcId;
    private String previousStatus;
    private String newStatus;
}
