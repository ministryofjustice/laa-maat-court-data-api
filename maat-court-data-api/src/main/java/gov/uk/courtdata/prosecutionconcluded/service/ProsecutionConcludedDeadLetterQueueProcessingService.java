package gov.uk.courtdata.prosecutionconcluded.service;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.exception.DeadLetterQueueReprocessingException;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.publisher.AwsStandardSqsPublisher;
import gov.uk.courtdata.service.QueueMessageLogService;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProsecutionConcludedDeadLetterQueueProcessingService {

    @Value("${cloud-platform.aws.sqs.queue.config.dlqReprocessingDelayMilliseconds}")
    private Integer dlqReprocessingDelay;

    private final AwsStandardSqsPublisher awsStandardSqsPublisher;

    private final Gson gson;

    private final ProsecutionConcludedService prosecutionConcludedService;

    private final QueueMessageLogService queueMessageLogService;

    public void execute(final String message, final Map<String, Object> messageHeaders) {
        String timestamp = getQueueEntryTimestamp(messageHeaders);
        Long timeInQueue = getTimeInQueue(timestamp);

        if (timeInQueue < dlqReprocessingDelay) {
            reprocessProsecutionConcludedMessage(message);
        }

        returnMessageToDeadLetterQueue(message, timestamp);
    }

    private String getQueueEntryTimestamp(Map<String, Object> messageHeaders) {
        return messageHeaders.getOrDefault("dlq_entry_time", messageHeaders.get("timestamp").toString()).toString();
    }

    private Long getTimeInQueue(String timestamp) {
        return (Instant.now().toEpochMilli() - Long.parseLong(timestamp));
    }

    private void returnMessageToDeadLetterQueue(String message, String timestamp) {
        Map<String, MessageAttributeValue> returnAttributes = new HashMap<>();
        returnAttributes.put("dlq_entry_time", new MessageAttributeValue().withStringValue(timestamp).withDataType("String"));
        awsStandardSqsPublisher.returnMessageToDeadLetterQueue(returnAttributes, message);
    }

    private void reprocessProsecutionConcludedMessage(final String prosecutionConcludedMessage) {
        try {
            ProsecutionConcluded prosecutionConcluded = gson.fromJson(prosecutionConcludedMessage, ProsecutionConcluded.class);

            log.info("Attempting re-processing of prosecution concluded message for MAAT ID {} from the dead letter queue ",
                    prosecutionConcluded.getMaatId());

            prosecutionConcludedService.execute(prosecutionConcluded);
        } catch (Exception exception) {
            String errorMessage = "Unable to process prosecution concluded message from the dead letter queue.";
            queueMessageLogService.createLog(MessageType.PROSECUTION_CONCLUDED_DEAD_LETTER, prosecutionConcludedMessage);
            Sentry.captureException(new DeadLetterQueueReprocessingException(errorMessage, exception));
        }
    }

}
