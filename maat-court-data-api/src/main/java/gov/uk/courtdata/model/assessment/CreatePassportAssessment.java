package gov.uk.courtdata.model.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreatePassportAssessment extends PassportAssessment {
    private Integer financialAssessmentId;
    private Integer usn;
    private String rtCode;
    private LocalDateTime dateCreated;
    private String userCreated;
}
