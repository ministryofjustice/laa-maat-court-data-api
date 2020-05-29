package gov.uk.courtdata.exception;

import com.google.gson.Gson;
import gov.uk.courtdata.model.LaaTransactionLogging;
import io.sentry.Sentry;
import io.sentry.event.Breadcrumb;
import io.sentry.event.BreadcrumbBuilder;
import io.sentry.event.UserBuilder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
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

        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("Exception: "+ex.getMessage()).setLevel(Breadcrumb.Level.ERROR).build());
        Sentry.clearContext();

        Sentry.getContext().addTag("laaTransactionId", MDC.get("laaTransactionId"));
        Sentry.getContext().addTag("caseUrn", MDC.get("caseUrn"));
        Sentry.getContext().setUser(new UserBuilder().setId(MDC.get("maatId")).build());

        String laaTransactionLogging = MDC.get("message");
        log.error("Exception StackTrace"+laaTransactionLogging, ex);

        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage(ex.getMessage()).setLevel(Breadcrumb.Level.ERROR).build());
        Sentry.capture(ex);
        Sentry.clearContext();
    }

    /**
     * This method will be called at the end when there is a successful message processing.
     * @param joinPoint
     */
    @AfterReturning(" execution(* gov.uk.courtdata.*.service.*.receive(..))  ")
    public void afterProcess(JoinPoint joinPoint) {
        log.info("Message from a queue has been processed successfully: {}", MDC.get("message"));

        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder()
                .setMessage("Message from a queue has been processed successfully.").setLevel(Breadcrumb.Level.INFO).build());
    }

    /**
     * This method will called every time at the of queue consumer, regardless of the outcome.
     * @param joinPoint
     */
    @After(" execution(* gov.uk.courtdata.*.service.*.receive(..))  ")
    public void afterProcessEnds(JoinPoint joinPoint) {

        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder()
                .setMessage("SQS Message processing finished.")
                .setLevel(Breadcrumb.Level.INFO).build());
        Sentry.capture("AWS SQS Message processed.");

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

        Gson gson = new Gson();
        LaaTransactionLogging laaTransactionLogging = gson.fromJson(message, LaaTransactionLogging.class);
        Sentry.getContext().addTag("laaTransactionId",
                laaTransactionLogging.getLaaTransactionId()!=null?laaTransactionLogging.getLaaTransactionId().toString():"");
        Sentry.getContext().addTag("caseUrn",
                laaTransactionLogging.getCaseUrn()!=null?laaTransactionLogging.getCaseUrn():"");
        Sentry.getContext().setUser(new UserBuilder()
                .setId(laaTransactionLogging.getMaatId()!=null?laaTransactionLogging.getMaatId().toString():"").build());
        log.info("Received a JSON Message and converted {}",laaTransactionLogging.toString());
        MDC.put("message", laaTransactionLogging.toString());

        MDC.put("caseUrn",laaTransactionLogging.getCaseUrn());
        MDC.put("laaTransactionId",laaTransactionLogging.getLaaTransactionId().toString());
        MDC.put("maatId",laaTransactionLogging.getMaatId().toString());

        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder()
                .setMessage("Received a JSON Message and converted " + laaTransactionLogging.toString() )
                .setLevel(Breadcrumb.Level.INFO).build());
    }
}
