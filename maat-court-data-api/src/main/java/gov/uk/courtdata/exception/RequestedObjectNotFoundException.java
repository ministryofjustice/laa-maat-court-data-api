package gov.uk.courtdata.exception;

public class RequestedObjectNotFoundException extends RuntimeException{

    public RequestedObjectNotFoundException(String aMessage) {
        super(aMessage);
    }

}
