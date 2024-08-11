package gov.uk.courtdata.dces.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFdcItemRequest {
    private int fdcId;
    private String itemType;
    private String adjustmentReason;
    private LocalDate dateCreated;
    private String userCreated;
    private String paidAsClaimed;
    private String latestCostInd;
}