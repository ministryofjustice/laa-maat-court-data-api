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
public class Plea {
    private UUID originatingHearingId;
    private String value;
    private String pleaDate;
}
