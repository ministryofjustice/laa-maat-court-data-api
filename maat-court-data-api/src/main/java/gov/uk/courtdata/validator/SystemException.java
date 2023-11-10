package gov.uk.courtdata.validator;

public class SystemException extends Exception {
    public SystemException() {
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Throwable rootCause) {
        super(rootCause);
    }

    public SystemException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
