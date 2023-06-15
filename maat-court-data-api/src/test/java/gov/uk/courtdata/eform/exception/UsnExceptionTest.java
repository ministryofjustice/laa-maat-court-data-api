package gov.uk.courtdata.eform.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsnExceptionTest {

    @Test
    void shouldReturnMessage_whenCreatingUsnExceptionWithMessage() {
        String exceptionMessage = "Sample exception message";
        UsnException exception = new UsnException(HttpStatus.OK, exceptionMessage);

        assertEquals(exceptionMessage, exception.getMessage());
    }

    @Test
    void shouldReturnHttpResponseCode_whenCreatingUsnExceptionWithHttpResponseCode() {
        HttpStatus httpStatus = HttpStatus.OK;
        UsnException exception = new UsnException(httpStatus, "Sample exception message");

        assertEquals(httpStatus, exception.getHttpResponseCode());
    }
}