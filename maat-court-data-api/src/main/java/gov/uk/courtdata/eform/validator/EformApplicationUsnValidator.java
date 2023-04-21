package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.model.EformApplication;
import org.springframework.stereotype.Component;

@Component
public class EformApplicationUsnValidator {

    public void validate(EformApplication eformApplication, Integer usn) {
        // TODO validate non-null usn and matching or throw exception
    }
}
