package gov.uk.courtdata.dces.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcorContributionRequest {
    private Integer recordsSent;
    @ToString.Exclude
    private String xmlContent;
    private Set<String> contributionIds;
    private String xmlFileName;
}