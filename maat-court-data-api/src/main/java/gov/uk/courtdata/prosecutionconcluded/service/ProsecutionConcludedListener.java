package gov.uk.courtdata.prosecutionconcluded.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.service.QueueMessageLogService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "feature.prosecution-concluded-listener.enabled", havingValue = "true")
public class ProsecutionConcludedListener {

    private final Gson gson;
    private final QueueMessageLogService queueMessageLogService;
    private final ProsecutionConcludedService prosecutionConcludedService;

    @SqsListener(value = "${cloud-platform.aws.sqs.queue.prosecutionConcluded}")
    public void receive(@Payload final String message, final @Headers MessageHeaders headers) {
        try {
            log.debug("message-id {}", headers.get("MessageId"));
            MDC.put(LoggingData.REQUEST_TYPE.getMdcKey(), MessageType.PROSECUTION_CONCLUDED.name());
            queueMessageLogService.createLog(MessageType.PROSECUTION_CONCLUDED, message);

            ProsecutionConcluded prosecutionConcluded = gson.fromJson(message, ProsecutionConcluded.class);
            prosecutionConcludedService.execute(prosecutionConcluded);

            log.info("CC Outcome is completed for  maat-id {}", prosecutionConcluded.getMaatId());
        } catch (ValidationException exception) {
            log.warn(exception.getMessage());
        }
    }
}