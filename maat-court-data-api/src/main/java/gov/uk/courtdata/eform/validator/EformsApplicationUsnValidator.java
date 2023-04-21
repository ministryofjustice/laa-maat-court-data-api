package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.model.EformsApplication;
import org.springframework.stereotype.Component;

@Component
public class EformsApplicationUsnValidator {

    public void validate(EformsApplication eformsApplication, Integer usn) {
        // TODO validate non-null usn and matching or throw exception
    }
}
