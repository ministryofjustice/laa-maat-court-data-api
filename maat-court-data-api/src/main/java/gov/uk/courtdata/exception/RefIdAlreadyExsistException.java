package gov.uk.courtdata.exception;

public class RefIdAlreadyExsistException extends RuntimeException {
    public RefIdAlreadyExsistException(String message) {
        super(message);
    }
}
