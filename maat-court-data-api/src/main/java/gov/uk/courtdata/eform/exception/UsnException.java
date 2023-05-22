package gov.uk.courtdata.eform.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsnException extends RuntimeException {
    
    private final HttpStatus httpResponseCode;

    private UsnException(HttpStatus httpStatus, String message) {
        super(message);
        httpResponseCode = httpStatus;
    }

    public static UsnException alreadyExists(String message) {
        return new UsnException(HttpStatus.BAD_REQUEST, message);
    }

    public static UsnException nonexistent(String message) {
        return new UsnException(HttpStatus.NOT_FOUND, message);
    }
}
