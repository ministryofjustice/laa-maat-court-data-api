package gov.uk.courtdata.eform.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class USNExceptionUtilTest {

    @Test
    void shouldReturnAlreadyExistsUsnException_whenAlreadyExistsIsCalledWithAUsn() {
        UsnException validationException = USNExceptionUtil.alreadyExists(7000001);

        assertEquals("The USN [7000001] already exists in the data store.", validationException.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, validationException.getHttpResponseCode());
    }

    @Test
    void shouldReturnNonexistentUsnException_whenNonexistentIsCalledWithMissingUsn() {
        UsnException validationException = USNExceptionUtil.nonexistent(123456);

        assertEquals("The USN [123456] does not exist in the data store.", validationException.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, validationException.getHttpResponseCode());
    }
}