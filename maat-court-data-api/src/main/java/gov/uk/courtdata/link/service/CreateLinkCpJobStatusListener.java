package gov.uk.courtdata.link.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.CpJobStatus;
import gov.uk.courtdata.service.QueueMessageLogService;

import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "feature.postMvpEnabled", havingValue = "true")
public class CreateLinkCpJobStatusListener {

    private final CreateLinkCpJobStatusService createLinkCpJobStatusService;
    private final Gson gson;
    private final QueueMessageLogService queueMessageLogService;

//This queue is not in use anymore - May 2023
@SqsListener(value = "${cloud-platform.aws.sqs.queue.createLinkCpStatusJob}")
    public void receive(@Payload final String message) {

        queueMessageLogService.createLog(MessageType.CREATE_LINK_CP_STATUS_JOB, message);

        CpJobStatus cpJobStatus = gson.fromJson(message, CpJobStatus.class);

        createLinkCpJobStatusService.execute(cpJobStatus);

        log.info("Create Link CP Status Job is Updated Successfully.");
    }
}
