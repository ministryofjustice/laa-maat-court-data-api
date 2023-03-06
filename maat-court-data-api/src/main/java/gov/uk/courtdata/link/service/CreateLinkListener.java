package gov.uk.courtdata.link.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.service.QueueMessageLogService;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Header;
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

    @SqsListener(value = "${cloud-platform.aws.sqs.queue.link}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receive(@Payload final String message, @Header("SenderId") final String senderId) {

        log.info("link request received from sender-id {}", senderId);
        MDC.put(LoggingData.REQUEST_TYPE.getValue(), MessageType.LINK.name());

        queueMessageLogService.createLog(MessageType.LINK,message);
        CaseDetails linkMessage = gson.fromJson(message, CaseDetails.class);

        createLinkService.saveAndLink(linkMessage);
    }
}
