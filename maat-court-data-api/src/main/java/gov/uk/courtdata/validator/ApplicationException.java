package gov.uk.courtdata.validator;

public class ApplicationException extends Exception {
    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(Throwable rootCause) {
        super(rootCause);
    }

    public ApplicationException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}