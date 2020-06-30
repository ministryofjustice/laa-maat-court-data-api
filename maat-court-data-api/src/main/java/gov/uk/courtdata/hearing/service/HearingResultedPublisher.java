package gov.uk.courtdata.hearing.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import gov.uk.courtdata.config.AmazonSQSConfig;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.util.LaaTransactionLoggingBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HearingResultedPublisher {

    @Value("${cloud-platform.aws.sqs.queue.hearingResulted}")
    private String sqsQueueName;

    @Value("${cloud-platform.aws.sqs.queue.config.messageDelay}")
    Integer delaySeconds;


    private final AmazonSQSConfig amazonSQSConfig;
    private final Gson gson;

    /**
     * Publishing a message to a hearing queue with a 15 minutes delay.
     * @param hearingResulted
     */
    public void publish (HearingResulted hearingResulted) {

        String logging = LaaTransactionLoggingBuilder.get(hearingResulted.toString()).toString();
        log.info("MAAT Record is locked. Publishing a message to the queue to process later.");
        log.info("Publishing to SQS Queue {} with logging meta-data {} " + sqsQueueName,logging);

        int counter = hearingResulted.getMessageRetryCounter();
        //TODO: remove this after testing on dev.
        Map<String,MessageAttributeValue> attributeValueMap = new HashMap<>();
        attributeValueMap.put("counter",
                new MessageAttributeValue().withStringValue((counter+1)+"").withDataType("String"));

        hearingResulted.setMessageRetryCounter(counter+1);

        String hearingResultedJSON = gson.toJson(hearingResulted);

        AmazonSQS amazonSQS = amazonSQSConfig.awsSqsClient();

        GetQueueUrlResult getQueueUrlResult = amazonSQS.getQueueUrl(sqsQueueName);
        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(getQueueUrlResult.getQueueUrl())
                .withMessageBody(hearingResultedJSON)
                .withDelaySeconds(delaySeconds)
                .withMessageAttributes(attributeValueMap);

        amazonSQS.sendMessage(request);
        log.info("Printing a message: "+request.toString());
        log.info("A CP hearing message has been published to the Queue {} with logging meta-data {}",sqsQueueName, logging);
    }
}
