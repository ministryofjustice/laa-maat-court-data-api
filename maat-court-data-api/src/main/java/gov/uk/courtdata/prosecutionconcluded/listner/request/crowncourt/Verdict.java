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
public class Verdict {
    private String verdictDate;
    private UUID originatingHearingId;
    private VerdictType verdictType;
}
