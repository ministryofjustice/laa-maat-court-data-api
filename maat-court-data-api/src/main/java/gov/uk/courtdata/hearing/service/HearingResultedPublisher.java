package gov.uk.courtdata.hearing.service;

import com.amazonaws.services.sqs.AmazonSQS;
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

    private final AmazonSQSConfig amazonSQSConfig;

    public void publish (HearingResulted hearingResulted) {

        String logging = LaaTransactionLoggingBuilder.get(hearingResulted).toString();
        log.info("MAAT Record is locked. Publishing a message to the queue to process later.");
        log.info("Publishing to SQS Queue {} with logging meta-data {} " + sqsQueueName,logging);


        int counter = hearingResulted.getMessageRetryCounter()!=null ? hearingResulted.getMessageRetryCounter()+1 : 0;
        Map<String,MessageAttributeValue> attributeValueMap = new HashMap<>();
        attributeValueMap.put("counter",
                new MessageAttributeValue().withStringValue(counter+"").withDataType("String"));

        hearingResulted.setMessageRetryCounter(counter);

        Gson gson = new Gson();
        String hearingResultedJSON = gson.toJson(hearingResulted);

        AmazonSQS amazonSQS = amazonSQSConfig.awsSqsClient();

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(amazonSQS.getQueueUrl(sqsQueueName).getQueueUrl())
                .withMessageBody(hearingResultedJSON)
                .withDelaySeconds(60)
                .withMessageAttributes(attributeValueMap);

        amazonSQS.sendMessage(request);
        log.info("Printing a message: "+request.toString());
        log.info("A CP hearing message has been published to the Queue {} with logging meta-data {}",sqsQueueName,logging);


    }

    }
