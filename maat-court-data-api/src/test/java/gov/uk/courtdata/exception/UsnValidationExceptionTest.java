package gov.uk.courtdata.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsnValidationExceptionTest {

    @Test
    void shouldReturnPreviouslySetExceptionMessage_whenGetMessageIsCalled() {
        String errorMessage = "Sample validation error message";
        UsnValidationException validationException = new UsnValidationException(errorMessage);

        assertEquals(errorMessage, validationException.getMessage());
    }
}