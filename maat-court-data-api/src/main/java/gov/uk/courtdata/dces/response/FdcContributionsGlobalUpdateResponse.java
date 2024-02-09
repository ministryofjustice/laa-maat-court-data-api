package gov.uk.courtdata.dces.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FdcContributionsGlobalUpdateResponse {
    private boolean successful;
    int numberOfUpdates;
}