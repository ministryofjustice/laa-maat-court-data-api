package gov.uk.courtdata.error;

public class MaatCourtDataException extends RuntimeException{

    public MaatCourtDataException(String aMessage, Exception aException) {
        super(aMessage, aException);
    }

    public MaatCourtDataException(String aMessage) {
        super(aMessage);
    }

}
