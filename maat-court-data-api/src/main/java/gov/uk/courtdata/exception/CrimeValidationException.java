package gov.uk.courtdata.exception;

import lombok.Getter;
import lombok.Setter;
import uk.gov.justice.laa.crime.error.ErrorMessage;

import java.util.List;

@Getter
@Setter
public class CrimeValidationException extends RuntimeException {
    private final transient List<ErrorMessage> exceptionMessages;

    public CrimeValidationException(List<ErrorMessage> exceptionMessages) {
        this.exceptionMessages = exceptionMessages;
    }
}