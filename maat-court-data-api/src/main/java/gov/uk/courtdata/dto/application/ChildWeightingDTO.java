package gov.uk.courtdata.dto.application;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ChildWeightingDTO extends GenericDTO {

    private Long id;
    private Long weightingId;
    private Integer lowerAgeRange;
    private Integer upperAgeRange;
    private Double weightingFactor;
    private Integer noOfChildren;

}
