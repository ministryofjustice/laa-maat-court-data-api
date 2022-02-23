package gov.uk.courtdata.prosecutionconcluded.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.google.gson.Gson;
import gov.uk.courtdata.config.AmazonSQSConfig;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.service.QueueMessageLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;


import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component

public class ProsecutionConcludedListener {

    private final Gson gson;

    private final ProsecutionConcludedService prosecutionConcludedService;

    private final QueueMessageLogService queueMessageLogService;

    private final AmazonSQSConfig amazonSQSConfig;

   @Value("${cloud-platform.aws.sqs.queue.prosecutionConcluded}")
    private String queueUrl;

    public void receive() {
        MDC.put(LoggingData.REQUEST_TYPE.getValue(), MessageType.PROSECUTION_CONCLUDED.name());

        AmazonSQS amazonSQS = amazonSQSConfig.awsSqsClient();

        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
                .withWaitTimeSeconds(20)
                .withMessageAttributeNames("trace-id");

        List<Message> sqsMessages = amazonSQS.receiveMessage(receiveMessageRequest).getMessages();
        if (sqsMessages!= null && sqsMessages.size()>0) {

            queueMessageLogService.createLog(MessageType.PROSECUTION_CONCLUDED, sqsMessages.get(0).getBody());

            ProsecutionConcluded prosecutionConcluded = gson.fromJson(sqsMessages.get(0).getBody(), ProsecutionConcluded.class);
            Map<String, MessageAttributeValue> messageAttributeValueMap = sqsMessages.get(0).getMessageAttributes();
            int messageRetryCounter = Integer.parseInt(messageAttributeValueMap.get("HearingRetryCounterValue").getStringValue());
            prosecutionConcluded.setRetryCounterForHearing(messageRetryCounter);

            prosecutionConcludedService.execute(prosecutionConcluded);
            log.info("CC Outcome is completed for  maat-id {}", prosecutionConcluded.getMaatId());

        } else {
            log.info("SQS message list is empty or null");
        }
    }
}