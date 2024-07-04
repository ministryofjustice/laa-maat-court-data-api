package gov.uk.courtdata.dces.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FdcContributionRequest {
    private int fdcId;
    private Integer repOrderId;
    private String status;
}