package gov.uk.courtdata.link.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.service.QueueMessageLogService;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * <code>CreateLinkListener</code> a JMS listener to consume create link messages from queue.
 */
@Slf4j
@XRayEnabled
@Service
@AllArgsConstructor
public class CreateLinkListener {

    private final CreateLinkService createLinkService;

    private final Gson gson;

    private final QueueMessageLogService queueMessageLogService;

    @SqsListener(value = "${cloud-platform.aws.sqs.queue.link}",
            deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receive(@Payload final String message,
                        final @Headers MessageHeaders headers) {

        try {
            log.debug("message-id {}", headers.get("MessageId"));
            MDC.put(LoggingData.REQUEST_TYPE.getValue(), MessageType.LINK.name());

            queueMessageLogService.createLog(MessageType.LINK, message);
            CaseDetails linkMessage = gson.fromJson(message, CaseDetails.class);

            createLinkService.saveAndLink(linkMessage);
        } catch (ValidationException exception) {
            log.warn(exception.getMessage());
        }
    }
}
