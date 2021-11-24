package gov.uk.courtdata.model.crowncourt;

import gov.uk.courtdata.model.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private UUID hearingIdWhereChangeOccured;
    private List<OffenceSummary> offenceSummaryList;
}