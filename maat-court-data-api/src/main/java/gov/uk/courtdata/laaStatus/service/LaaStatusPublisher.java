package gov.uk.courtdata.laaStatus.service;

import com.google.gson.Gson;
import gov.uk.courtdata.model.CaseDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaaStatusPublisher {

    private final JmsTemplate defaultJmsTemplate;
    private final Gson gson;

    @Value("${cloud-platform.aws.sqs.queue.laaStatus}")
    private String sqsQueueName;

    public void publish(CaseDetails caseDetails) {

        log.info("Publishing to SQS Queue {} with laa-transaction-id {} " + sqsQueueName, caseDetails.getLaaTransactionId());
        String laaStatusUpdateJSON = gson.toJson(caseDetails);
        defaultJmsTemplate.convertAndSend(sqsQueueName, laaStatusUpdateJSON);
        log.info("A JSON Message with laa-transaction-id {} has been published to the Queue {}", caseDetails.getLaaTransactionId(), sqsQueueName);
    }
}
