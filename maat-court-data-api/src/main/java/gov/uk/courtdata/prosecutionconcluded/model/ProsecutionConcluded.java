package gov.uk.courtdata.prosecutionconcluded.model;

import gov.uk.courtdata.model.Metadata;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProsecutionConcluded {

    private Integer maatId;
    private UUID defendantId;
    private UUID prosecutionCaseId;
    private boolean isConcluded;
    private UUID hearingIdWhereChangeOccurred;
    private List<OffenceSummary> offenceSummary;
    private int messageRetryCounter;
    private int retryCounterForHearing;
    private Metadata metadata;

}