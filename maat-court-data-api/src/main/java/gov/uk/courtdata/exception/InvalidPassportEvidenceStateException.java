package gov.uk.courtdata.exception;

public class InvalidPassportEvidenceStateException extends RuntimeException {
    public InvalidPassportEvidenceStateException(String message) {
        super(message);
    }
}
