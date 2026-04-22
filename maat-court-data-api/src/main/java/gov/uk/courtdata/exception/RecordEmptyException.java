package gov.uk.courtdata.exception;

public class RecordEmptyException extends RuntimeException {
    public RecordEmptyException(String message) {
        super(message);
    }
}
