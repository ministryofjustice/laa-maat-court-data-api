package gov.uk.courtdata.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UsnException extends RuntimeException {

    private static final String ALREADY_EXISTS_MESSAGE_FORMAT = "The USN [%d] already exists in the data store.";
    private static final String NONEXISTENT_MESSAGE_FORMAT = "The USN [%d] does not exist in the data store.";

    private final HttpStatus httpResponseCode;

    private UsnException(HttpStatus httpStatus, String message) {
        super(message);
        httpResponseCode = httpStatus;
    }

    public static UsnException alreadyExists(int usn) {
        return new UsnException(HttpStatus.BAD_REQUEST,
                String.format(ALREADY_EXISTS_MESSAGE_FORMAT, usn));
    }

    public static UsnException nonexistent(Integer usn) {
        return new UsnException(HttpStatus.NOT_FOUND,
                String.format(NONEXISTENT_MESSAGE_FORMAT, usn));
    }
}
