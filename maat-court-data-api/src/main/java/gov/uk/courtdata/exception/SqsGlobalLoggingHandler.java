package gov.uk.courtdata.exception;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.LaaTransactionLogging;
import io.sentry.Sentry;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SqsGlobalLoggingHandler {

    private static final String SQS_LISTENER_IN_COURT_DATA = "within(gov.uk.courtdata..*) && @annotation(io.awspring.cloud.sqs.annotation.SqsListener)";
    private static final String MESSAGE_FOR_SQS_LISTENER_IN_COURT_DATA =
        SQS_LISTENER_IN_COURT_DATA + " && args(message,..) ";

    private final Gson gson;

    /**
     * This method will execute whenever an exception is thrown by any class in a service package
     * from a method named 'receive'.
     * If we are adding new queue listener then we should follow the same existing pattern.
     */
    @AfterThrowing(pointcut = SQS_LISTENER_IN_COURT_DATA, throwing = "ex")
    public void afterThrowingHearingDetail(JoinPoint joinPoint, RuntimeException ex) {

        log().info("Exception thrown - {} ", ex.getMessage());
        log().error("Exception StackTrace", ex);
    }

    Logger log() {
        return log;
    }

    /**
     * This method will be called at the end, following the successful processing of a message.
     */
    @AfterReturning(SQS_LISTENER_IN_COURT_DATA)
    public void afterProcess(JoinPoint joinPoint) {
        log().info("Message from a queue has been processed successfully");
    }

    /**
     * This method will be called every time at the of queue consumer, regardless of the outcome.
     */
    @After(SQS_LISTENER_IN_COURT_DATA)
    public void afterProcessEnds(JoinPoint joinPoint) {
        log().info("Message processing finished.");

    }

    /**
     * This method will log the message and also put the message to MDC fo logging in the case of any failure (e.g. exception occur).
     * This method will be called automatically for all the queue (jms) listeners. More specifically classes within
     * service package where method name is receive. For any new queue listener we should follow the same naming convention.
     *
     */
    @Before(MESSAGE_FOR_SQS_LISTENER_IN_COURT_DATA)
    public void beforeQueueMessageRec(JoinPoint joinPoint, String message) {

        LaaTransactionLogging laaTransactionLogging = gson.fromJson(message, LaaTransactionLogging.class);
        String laaTransactionIdStr;

        if (laaTransactionLogging.getMetadata() != null
                && laaTransactionLogging.getMetadata().getLaaTransactionId() != null) {
            laaTransactionIdStr = laaTransactionLogging.getMetadata().getLaaTransactionId().toString();
        } else {
            laaTransactionIdStr = UUID.randomUUID().toString();
        }

        Sentry.configureScope(scope -> {
            scope.setTag(LoggingData.CASE_URN.getKey(),
                laaTransactionLogging.getCaseUrn() != null ? laaTransactionLogging.getCaseUrn()
                    : "");
            scope.setTag(LoggingData.LAA_TRANSACTION_ID.getKey(), laaTransactionIdStr);
            scope.setTag(LoggingData.MAATID.getKey(),
                laaTransactionLogging.getMaatId().toString());
        });

        LoggingData.MESSAGE.putInMDC(laaTransactionLogging.toString());
        LoggingData.CASE_URN.putInMDC(
            laaTransactionLogging.getCaseUrn() != null ? laaTransactionLogging.getCaseUrn() : "");
        LoggingData.LAA_TRANSACTION_ID.putInMDC(laaTransactionIdStr);
        LoggingData.MAATID.putInMDC(
            laaTransactionLogging.getMaatId() != null ? laaTransactionLogging.getMaatId().toString()
                : "-");
        log().info("Received a json payload from a queue and converted.");
    }
}
