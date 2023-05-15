package gov.uk.courtdata.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsnExceptionTest {

    @Test
    void shouldReturnAlreadyExistsUsnException_whenAlreadyExistsIsCalledWithAUsn() {
        UsnException validationException = UsnException.alreadyExists(7000001);

        assertEquals("The USN [7000001] already exists in the data store.", validationException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, validationException.getHttpResponseCode());
    }

    @Test
    void shouldReturnNonexistentUsnException_whenNonexistentIsCalledWithNullUsn() {
        UsnException validationException = UsnException.nonexistent(null);

        assertEquals("The USN [null] does not exist in the data store.", validationException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, validationException.getHttpResponseCode());
    }

    @Test
    void shouldReturnNonexistentUsnException_whenNonexistentIsCalledWithMissingUsn() {
        UsnException validationException = UsnException.nonexistent(123456);

        assertEquals("The USN [123456] does not exist in the data store.", validationException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, validationException.getHttpResponseCode());
    }
}