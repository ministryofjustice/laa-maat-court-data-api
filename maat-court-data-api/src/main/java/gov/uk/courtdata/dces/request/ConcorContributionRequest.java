package gov.uk.courtdata.dces.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcorContributionRequest {
    private Integer recordsSent;
    private String xmlContent;
    private List<String> contributionIds;
    private String xmlFileName;
}