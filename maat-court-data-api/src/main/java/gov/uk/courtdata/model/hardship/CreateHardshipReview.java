package gov.uk.courtdata.model.hardship;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateHardshipReview extends HardshipReview {

    private Integer repId;
    private String userCreated;
    private String courtType;
    private Integer financialAssessmentId;
}
