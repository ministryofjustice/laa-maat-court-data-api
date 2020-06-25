package gov.uk.courtdata.hearing.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import gov.uk.courtdata.jms.SqsProperties;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.util.LaaTransactionLoggingBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class HearingResultedPublisher {

    private final JmsTemplate defaultJmsTemplate;
    private final Gson gson;

    @Value("${cloud-platform.aws.sqs.queue.hearingResulted}")
    private String sqsQueueName;

    private final SqsProperties sqsProperties;

    public void publish (HearingResulted hearingResulted) {

        String logging = LaaTransactionLoggingBuilder.get(hearingResulted).toString();
        log.info("MAAT Record is locked. Publishing a message to the queue to process later.");
        log.info("Publishing to SQS Queue {} with logging meta-data {} " + sqsQueueName,logging);

        String hearingResultedJSON = gson.toJson(hearingResulted);
        //adding a counter to one.
        hearingResulted.setMessageRetryCounter(hearingResulted.getMessageRetryCounter()+1);

        int counter = hearingResulted.getMessageRetryCounter()+1;
        Map<String,MessageAttributeValue> attributeValueMap = new HashMap<>();
        attributeValueMap.put("counter",
                new MessageAttributeValue().withStringValue(counter+"").withDataType("String"));

        AmazonSQS amazonSQS = awsSqsClient();

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(amazonSQS.getQueueUrl(sqsQueueName).getQueueUrl())
                .withMessageBody(hearingResultedJSON)
                .withDelaySeconds(900)
                .withMessageAttributes(attributeValueMap);


        amazonSQS.sendMessage(request);

        //add a delay




        defaultJmsTemplate.convertAndSend(sqsQueueName, hearingResultedJSON);
        log.info("A JSON Message has been published to the Queue {} with logging meta-data {}",sqsQueueName,logging);


    }

    private AmazonSQS awsSqsClient() {
        return
                AmazonSQSClientBuilder.standard()
                        .withCredentials(new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(sqsProperties.getAccesskey(), sqsProperties.getSecretkey())))
                        .withRegion(Regions.fromName(sqsProperties.getRegion()))
                        .build();
    }
}
