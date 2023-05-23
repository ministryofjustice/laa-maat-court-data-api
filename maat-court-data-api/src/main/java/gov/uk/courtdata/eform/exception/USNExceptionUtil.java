package gov.uk.courtdata.eform.exception;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class USNExceptionUtil {

    private static final String ALREADY_EXISTS_MESSAGE_FORMAT = "The USN [%d] already exists in the data store.";
    private static final String NONEXISTENT_MESSAGE_FORMAT = "The USN [%d] does not exist in the data store.";

    public UsnException alreadyExists(int usn) {
        return new UsnException(HttpStatus.BAD_REQUEST,
                String.format(ALREADY_EXISTS_MESSAGE_FORMAT, usn));
    }

    public UsnException nonexistent(int usn) {
        return new UsnException(HttpStatus.NOT_FOUND,
                String.format(NONEXISTENT_MESSAGE_FORMAT, usn));
    }
}
