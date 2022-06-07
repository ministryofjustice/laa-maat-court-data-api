package gov.uk.courtdata.prosecutionconcluded.service;

import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.publisher.AwsStandardSqsPublisher;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProsecutionConcludedDeadLetterQueueService {

    private final ProsecutionConcludedDeadLetterQueueProcessingService prosecutionConcludedDeadLetterQueueProcessingService;

    @JmsListener(destination = "${cloud-platform.aws.sqs.queue.prosecutionConcludedDeadLetter}")
    public void receive(@Payload final String message, @Headers final Map<String, Object> messageHeaders) throws JMSException {
        prosecutionConcludedDeadLetterQueueProcessingService.execute(message, messageHeaders);
    }
}
