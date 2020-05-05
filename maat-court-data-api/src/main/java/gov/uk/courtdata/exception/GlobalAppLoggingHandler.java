package gov.uk.courtdata.exception;

import com.google.gson.Gson;
import gov.uk.courtdata.model.LaaTransactionLogging;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class GlobalAppLoggingHandler {

    /**
     * This method will execute whenever a exception occour in any of the following (service) package and a class has method name receive.
     * If we are adding new queue listener then we should follow the same existing pattern.
     * @param joinPoint
     * @param ex
     */
    @AfterThrowing(pointcut = "execution(* gov.uk.courtdata.*.service.*.receive(..))", throwing = "ex")
    public void afterThrowingHearingDetail(JoinPoint joinPoint, RuntimeException ex) {

        Gson gson = new Gson();
        LaaTransactionLogging laaTransactionLogging = gson.fromJson(MDC.get("message"), LaaTransactionLogging.class);

        log.info("Failed: Exception occur  - " + laaTransactionLogging.toString());
        log.error("Exception str "+ ex.toString());

    }

    /**
     * This method will log the message and also put the message to MDC fo logging in the case of any failure (e.g. exception occur).
     * This method will be called automatically for all the queue (jms) listeners. More specifically classes within
     * service package where method name is receive. For any new queue listener we should follow the same naming convenction.
     * @param joinPoint
     * @param message
     */
    @Before(" execution(* gov.uk.courtdata.*.service.*.receive(..))  && args(message,..) ")
    public void beforeQueueMessageRec(JoinPoint joinPoint, String message) {

        log.info("Received JSON Message from a queue: " + joinPoint.getClass().getName() );
        MDC.put("message", message);
    }



}
