package gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OffenceSummary {
    private UUID offenceId;
    private String offenceCode;
    private boolean proceedingConcluded;
    private Plea plea;
    private Verdict verdict;
    private String proceedingsConcludedChangedDate;
}
