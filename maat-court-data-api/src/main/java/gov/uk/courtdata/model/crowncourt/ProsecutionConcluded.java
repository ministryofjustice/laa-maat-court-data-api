package gov.uk.courtdata.model.crowncourt;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProsecutionConcluded {
    private Integer maatId;
    private Integer defendantId;
    private UUID prosecutionCaseId;
    private boolean concluded;
    private UUID hearingIdWhereChangeOccured;
    private List<OffenceSummary> offenceSummaryList;
}