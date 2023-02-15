package gov.uk.courtdata.eformsApplication.builder;

import gov.uk.courtdata.eformsApplication.dto.EformsApplicationDTO;
import gov.uk.courtdata.eformsApplication.dto.EformsStagingDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EformsStagingBuilder {

    public EformsStagingDTO build(EformsApplicationDTO eformsApplicationDTO) {
        return EformsStagingDTO.builder().build();
    }
}
