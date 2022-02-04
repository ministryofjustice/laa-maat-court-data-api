package gov.uk.courtdata.prosecutionconcluded.validator;

import gov.uk.courtdata.exception.ValidationException;


import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
public class ProsecutionConcludedValidator {

    public void validateRequestObject(ProsecutionConcluded prosecutionConcluded) {
        if (prosecutionConcluded == null
                || prosecutionConcluded.getOffenceSummary() == null
                || prosecutionConcluded.getOffenceSummary().isEmpty()
                || prosecutionConcluded.getMaatId() == null)
            throw new ValidationException("Payload is not available or null. ");
    }

    public Optional<Void> validateOuCode(String ouCode) {

        if (!isBlank(ouCode)) {
            return Optional.empty();
        } else {
            throw new ValidationException("OU Code is missing.");
        }
    }
}
