package gov.uk.courtdata.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChildWeightHistoryDTO {
    private Integer facwId;
    private Integer childWeightingId;
    private Integer noOfChildren;
    private String userCreated;
}