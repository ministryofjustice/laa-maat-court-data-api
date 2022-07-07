package gov.uk.courtdata.model.assessment;

import gov.uk.courtdata.model.authorization.UserSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PostProcessing {
    private Integer repId;
    private UserSession user;
}
