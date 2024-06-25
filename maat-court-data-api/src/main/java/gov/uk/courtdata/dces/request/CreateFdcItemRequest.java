package gov.uk.courtdata.dces.request;

import gov.uk.courtdata.dto.application.FdcItemDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFdcItemRequest {
    private int fdcId;
    private String itemType; //maybe enum / existing class
    private String adjustmentReason;
    private LocalDateTime dateCreated;
    private String userCreated;
    private String paidAsClaimed; //maybe enum
    private String latestCostInd;
}