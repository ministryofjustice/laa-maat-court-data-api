package gov.uk.courtdata.exception;

public class RecordsAlreadyExistException extends RuntimeException {
    public RecordsAlreadyExistException(String message) {
        super(message);
    }
}
