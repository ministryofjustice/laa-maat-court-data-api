package gov.uk.courtdata.model.crowncourt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProsecutionConcluded {
    private Integer defendantId;
    private UUID prosecutionCaseId;
    private boolean IsConcluded;
    private UUID hearingIdWhereChangeOccured;
}