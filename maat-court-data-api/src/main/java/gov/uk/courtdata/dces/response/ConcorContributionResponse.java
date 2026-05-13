package gov.uk.courtdata.dces.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcorContributionResponse {
    private Integer concorContributionId;

    @ToString.Exclude
    private String xmlContent;
}
