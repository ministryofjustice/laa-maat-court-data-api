package gov.uk.courtdata.unlink.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.service.QueueMessageLogService;
import gov.uk.courtdata.unlink.processor.UnLinkProcessor;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Slf4j
@XRayEnabled
@AllArgsConstructor
@Service
public class UnlinkListener {

    private final Gson gson;

    private final UnLinkProcessor unLinkProcessor;

    private final QueueMessageLogService queueMessageLogService;

    @SqsListener(value = "${cloud-platform.aws.sqs.queue.unlink}",
            deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receive(@Payload final String message,
                        final @Headers MessageHeaders headers) {

        try {
            log.debug("message-id {}", headers.get("MessageId"));
            MDC.put(LoggingData.REQUEST_TYPE.getValue(), MessageType.UNLINK.name());
            queueMessageLogService.createLog(MessageType.UNLINK, message);
            Unlink unlink = gson.fromJson(message, Unlink.class);
            unLinkProcessor.process(unlink);
        } catch (ValidationException exception) {
            log.warn(exception.getMessage());
        }
    }
}
