package gov.uk.courtdata.dces.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ContributionFileErrorResponse {
    private final Integer contributionFileId;
    private final Integer contributionId;
    private final Integer repId;
    private final String errorText;
    private final String fixAction;
    private final Integer fdcContributionId;
    private final Integer concorContributionId;
    private final LocalDateTime dateCreated;
}
