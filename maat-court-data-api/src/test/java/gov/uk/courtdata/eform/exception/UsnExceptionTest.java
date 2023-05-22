package gov.uk.courtdata.eform.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsnExceptionTest {

    private static final String ALREADY_EXISTS_MESSAGE_FORMAT = "The USN [%d] already exists in the data store.";
    private static final String NONEXISTENT_MESSAGE_FORMAT = "The USN [%d] does not exist in the data store.";

    @Test
    void shouldReturnAlreadyExistsUsnException_whenAlreadyExistsIsCalledWithAUsn() {
        UsnException validationException = UsnException.alreadyExists(String.format(ALREADY_EXISTS_MESSAGE_FORMAT, 7000001));

        assertEquals("The USN [7000001] already exists in the data store.", validationException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, validationException.getHttpResponseCode());
    }

    @Test
    void shouldReturnNonexistentUsnException_whenNonexistentIsCalledWithMissingUsn() {
        UsnException validationException = UsnException.nonexistent(String.format(NONEXISTENT_MESSAGE_FORMAT, 123456));

        assertEquals("The USN [123456] does not exist in the data store.", validationException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, validationException.getHttpResponseCode());
    }
}