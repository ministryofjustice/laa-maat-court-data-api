package gov.uk.courtdata.eform.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsnException extends RuntimeException {

    private final HttpStatus httpResponseCode;

    public UsnException(HttpStatus httpStatus, String message) {
        super(message);
        httpResponseCode = httpStatus;
    }
}
