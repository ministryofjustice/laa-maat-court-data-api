package gov.uk.courtdata.model.assessment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAppDateCompleted {

    private Integer repId;
    private LocalDateTime assessmentDateCompleted;

}
