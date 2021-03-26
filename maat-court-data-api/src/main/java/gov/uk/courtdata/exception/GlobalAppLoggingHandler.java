package gov.uk.courtdata.exception;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.LaaTransactionLogging;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalAppLoggingHandler {

    private final Gson gson;

    /**
     * This method will execute whenever a exception occour in any of the following (service) package and a class has method name receive.
     * If we are adding new queue listener then we should follow the same existing pattern.
     */
    @AfterThrowing(pointcut = "execution(* gov.uk.courtdata.*.service.*.receive(..))", throwing = "ex")
    public void afterThrowingHearingDetail(JoinPoint joinPoint, RuntimeException ex) {

        log.info("Exception thrown - {} ", ex.getMessage());
        log.error("Exception StackTrace", ex);
    }

    /**
     * This method will be called at the end when there is a successful message processing.
     */
    @AfterReturning(" execution(* gov.uk.courtdata.*.service.*.receive(..))  ")
    public void afterProcess(JoinPoint joinPoint) {
        log.info("Message from a queue has been processed successfully");
    }

    /**
     * This method will called every time at the of queue consumer, regardless of the outcome.
     */
    @After(" execution(* gov.uk.courtdata.*.service.*.receive(..))  ")
    public void afterProcessEnds(JoinPoint joinPoint) {
        log.info("Message processing finished.");
    }

    /**
     * This method will log the message and also put the message to MDC fo logging in the case of any failure (e.g. exception occur).
     * This method will be called automatically for all the queue (jms) listeners. More specifically classes within
     * service package where method name is receive. For any new queue listener we should follow the same naming convention.
     *
     */
    @Before(" execution(* gov.uk.courtdata.*.service.*.receive(..))  && args(message,..) ")
    public void beforeQueueMessageRec(JoinPoint joinPoint, String message) {

        LaaTransactionLogging laaTransactionLogging = gson.fromJson(message, LaaTransactionLogging.class);

        Sentry.configureScope(scope -> {
            scope.setTag(LoggingData.MESSAGE.getValue(), laaTransactionLogging.toString());
            scope.setTag(LoggingData.CASE_URN.getValue(), laaTransactionLogging.getCaseUrn());
            scope.setTag(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionLogging.getLaaTransactionId() != null ? laaTransactionLogging.getLaaTransactionId().toString() : "");
            scope.setTag(LoggingData.MAATID.getValue(), laaTransactionLogging.getMaatId().toString());
        });

        MDC.put(LoggingData.MESSAGE.getValue(), laaTransactionLogging.toString());
        MDC.put(LoggingData.CASE_URN.getValue(), laaTransactionLogging.getCaseUrn());
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionLogging.getLaaTransactionId() != null ? laaTransactionLogging.getLaaTransactionId().toString() : "");
        MDC.put(LoggingData.MAATID.getValue(), laaTransactionLogging.getMaatId().toString());
        log.info("Received a json payload from a queue and converted.");
    }
}