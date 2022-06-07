package gov.uk.courtdata.exception;

public class DeadLetterQueueReprocessingException extends RuntimeException{

    public DeadLetterQueueReprocessingException(String aMessage, Throwable cause) {
        super(aMessage, cause);
    }

}
