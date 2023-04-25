package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * The responsibility of this class is to validate eForms types i.e. EFORMS_STAGING.TYPE
 */
@Component
public class TypeValidator {

    public void validate(String type) throws ValidationException {
        if (StringUtils.isBlank(type)) {
            String message = String.format("Expected non-null, non-empty, non-blank type but found [%s]", type);
            throw new ValidationException(message);
        }
    }
}
