package gov.uk.courtdata.link.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.CpJobStatus;
import gov.uk.courtdata.service.QueueMessageLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@ConditionalOnProperty(value = "feature.postMvpEnabled", havingValue = "true")
public class CreateLinkCpJobStatusListener {

    private final CreateLinkCpJobStatusService createLinkCpJobStatusService;
    private final Gson gson;
    private final QueueMessageLogService queueMessageLogService;
    //ToDo: This SQS has been removed for now at the Cloud Platform. Its a work in progress so keeping it for now.
    //@JmsListener(destination = "${cloud-platform.aws.sqs.queue.createLinkCpStatusJob}")
    public void receive(@Payload final String message) {

        queueMessageLogService.createLog(MessageType.CREATE_LINK_CP_STATUS_JOB, message);

        CpJobStatus cpJobStatus = gson.fromJson(message, CpJobStatus.class);

        createLinkCpJobStatusService.execute(cpJobStatus);

        log.info("Create Link CP Status Job is Updated Successfully.");
    }
}
