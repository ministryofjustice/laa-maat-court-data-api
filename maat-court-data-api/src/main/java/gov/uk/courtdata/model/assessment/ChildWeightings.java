package gov.uk.courtdata.model.assessment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChildWeightings {
    private Long childWeightingId;
    private Integer noOfChildren;
}
