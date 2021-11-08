package gov.uk.courtdata.laastatus.service;

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
public class LaaStatusJobListener {

    private final LaaStatusJobService laaStatusJobService;
    private final Gson gson;
    private final QueueMessageLogService queueMessageLogService;
    //ToDo: This SQS has been removed for now at the Cloud Platform. Its a work in progress so keeping it for now.
    //@JmsListener(destination = "${cloud-platform.aws.sqs.queue.laaStatusJob}")
    public void receive(@Payload final String message) {

        queueMessageLogService.createLog(MessageType.LAA_STATUS_JOB, message);

        CpJobStatus cpJobStatus = gson.fromJson(message, CpJobStatus.class);

        laaStatusJobService.execute(cpJobStatus);

        log.info("Laa Status Job is Updated Successfully for MAAT ID - {} , Txn ID - {}",
                cpJobStatus.getMaatId(), cpJobStatus.getLaaStatusTransactionId());

    }
}
