package gov.uk.courtdata.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChildWeightHistoryDTO {
    private Integer fashId;
    private Integer facwId;
    private Integer childWeightingId;
    private Integer noOfChildren;
    private String userCreated;
}