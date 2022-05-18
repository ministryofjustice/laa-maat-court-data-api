package gov.uk.courtdata.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChildWeightHistoryDTO {
    private FinancialAssessmentsHistoryDTO financialAssessmentsHistory;
    private Integer facwId;
    private Integer childWeightingId;
    private Integer noOfChildren;
    private String userCreated;
}