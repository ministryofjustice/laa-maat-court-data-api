package gov.uk.courtdata.prosecutionconcluded.listner.request;

import gov.uk.courtdata.exception.ValidationException;
import static org.apache.commons.lang3.StringUtils.isBlank;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProsecutionConcludedValidator {

    public Optional<Void> validateOuCode(String ouCode) {

        if (!isBlank(ouCode)) {
            return Optional.empty();
        } else {
            throw new ValidationException("OU Code is missing.");
        }
    }
}
