package gov.uk.courtdata.dto.application;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetHardshipDTO {

    private UserDTO userDTO;
    private Integer hardshipAssessmentId;

}
