package gov.uk.courtdata.model.assessment;

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
public class UpdatePassportAssessment extends PassportAssessment {

    private Integer id;
    private String userModified;
}
