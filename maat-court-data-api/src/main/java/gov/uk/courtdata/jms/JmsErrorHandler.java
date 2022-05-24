package gov.uk.courtdata.jms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

/**
 * A <code>SQSErrorHandler</code> handles all failures on JMS listener
 * and logs message.
 */
@Slf4j
@Service
public class JmsErrorHandler implements ErrorHandler {


    @Override
    public void handleError(Throwable t) {

        log.warn("In default JMS error handler...");
        log.error("Error Message : {}", t.getMessage());
        throw new RuntimeException(t.getMessage());
    }

}
