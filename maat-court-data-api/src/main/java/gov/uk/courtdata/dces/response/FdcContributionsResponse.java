package gov.uk.courtdata.dces.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FdcContributionsResponse {
    private List<FdcContributionEntry> fdcContributions;
}