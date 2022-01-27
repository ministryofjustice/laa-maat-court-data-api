package gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProsecutionConcluded {
    private Integer maatId;
    private Integer defendantId;
    private UUID prosecutionCaseId;
    private boolean concluded;
    private UUID hearingIdWhereChangeOccurred;
    private List<OffenceSummary> offenceSummaryList;
    private int messageRetryCounter;
}