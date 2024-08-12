package gov.uk.courtdata.aspect;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.LaaTransactionLogging;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SqsGlobalLogging {

  private static final String SQS_LISTENER_IN_COURT_DATA = "within(gov.uk.courtdata..*) && @annotation(io.awspring.cloud.sqs.annotation.SqsListener)";
  private static final String MESSAGE_FOR_SQS_LISTENER_IN_COURT_DATA =
      SQS_LISTENER_IN_COURT_DATA + " && args(message,..) ";

  private final Gson gson;

  /**
   * This method will execute whenever an exception is thrown by any method within
   * 'gov.uk.courtdata' (or any sub-package) and has been annotated with '@SqsListener'.
   */
  @AfterThrowing(pointcut = SQS_LISTENER_IN_COURT_DATA, throwing = "ex")
  public void afterThrowingHearingDetail(JoinPoint joinPoint, RuntimeException ex) {
    log.error("Exception thrown when processing message: {}", ex.getMessage(), ex);
  }

  /**
   * This method will be called at the end, following the successful processing of a message.
   */
  @AfterReturning(SQS_LISTENER_IN_COURT_DATA)
  public void afterProcess(JoinPoint joinPoint) {
    log.info("Message from queue has been successfully processed");
  }

  /**
   * This method will be called every time at the of queue consumer, regardless of the outcome.
   */
  @After(SQS_LISTENER_IN_COURT_DATA)
  public void afterProcessEnds(JoinPoint joinPoint) {
    log.info("Message processing completed");
    log.debug("About to clear down the MDC");
    MDC.clear();
  }

  /**
   * This method will log the message and also put the message into the MDC for logging in the case
   * of any failure (e.g. an Exception is thrown). This method will be called for all the queue
   * (SQS) listeners.
   */
  @Before(MESSAGE_FOR_SQS_LISTENER_IN_COURT_DATA)
  public void beforeQueueMessageRec(JoinPoint joinPoint, String message) {

    LaaTransactionLogging laaTransactionLogging = gson.fromJson(message,
        LaaTransactionLogging.class);
    String laaTransactionIdStr;

    if (laaTransactionLogging.getMetadata() != null
        && laaTransactionLogging.getMetadata().getLaaTransactionId() != null) {
      laaTransactionIdStr = laaTransactionLogging.getMetadata().getLaaTransactionId();
    } else {
      laaTransactionIdStr = UUID.randomUUID().toString();
    }

    LoggingData.CASE_URN.putInMDC(laaTransactionLogging.getCaseUrn());
    LoggingData.LAA_TRANSACTION_ID.putInMDC(laaTransactionIdStr);
    LoggingData.MAAT_ID.putInMDC(laaTransactionLogging.getMaatId());

    log.info("Received JSON payload from the queue");
  }
}
