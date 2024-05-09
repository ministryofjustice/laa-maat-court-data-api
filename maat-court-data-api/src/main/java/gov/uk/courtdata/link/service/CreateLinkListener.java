package gov.uk.courtdata.link.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.service.QueueMessageLogService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * <code>CreateLinkListener</code> a JMS listener to consume create link messages from queue.
 */
@Slf4j
@Service
@AllArgsConstructor
public class CreateLinkListener {

    private final CreateLinkService createLinkService;

    private final Gson gson;

    private final QueueMessageLogService queueMessageLogService;

    @SqsListener(value = "${cloud-platform.aws.sqs.queue.link}")
    public void receive(@Payload final String message,
                        final @Headers MessageHeaders headers) {

        try {
            log.debug("message-id {}", headers.get("MessageId"));
            LoggingData.REQUEST_TYPE.putInMDC(MessageType.LINK.name());

            queueMessageLogService.createLog(MessageType.LINK, message);
            CaseDetails linkMessage = gson.fromJson(message, CaseDetails.class);

            createLinkService.saveAndLink(linkMessage);
        } catch (ValidationException exception) {
            log.warn(exception.getMessage());
        }
    }
}
