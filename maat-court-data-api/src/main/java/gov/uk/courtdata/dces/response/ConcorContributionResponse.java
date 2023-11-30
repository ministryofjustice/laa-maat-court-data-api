package gov.uk.courtdata.dces.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcorContributionResponse {
    private Integer concorContributionId;
    @ToString.Exclude
    private String xmlContent;
}