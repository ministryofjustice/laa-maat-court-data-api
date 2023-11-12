package gov.uk.courtdata.dces.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcorContributionRequest {
    private Integer recordsSent;
    private String xmlContent;
}