package gov.uk.courtdata.hearing.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import gov.uk.courtdata.config.AmazonSQSConfig;
import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HearingResultedPublisher {

    @Value("${cloud-platform.aws.sqs.queue.hearingResulted}")
    private String sqsQueueName = "";

    @Value("${cloud-platform.aws.sqs.queue.config.messageDelay}")
    private Integer delaySeconds;

    private final AmazonSQSConfig amazonSQSConfig;
    private final Gson gson;

    /**
     * Publishing a message to a hearing queue with a 15 minutes delay.
     * @param hearingResulted message payload
     */
    public void publish (HearingResulted hearingResulted) {

        log.info("MAAT Record is locked. Publishing a message to the hearing queue to process later.");
        log.info("Publishing to SQS Queue {} ", sqsQueueName);

        int counter = hearingResulted.getMessageRetryCounter();
        hearingResulted.setMessageRetryCounter(counter+1);
        String hearingResultedJSON = gson.toJson(hearingResulted);

        AmazonSQS amazonSQS = amazonSQSConfig.awsSqsClient();
        GetQueueUrlResult getQueueUrlResult = amazonSQS.getQueueUrl(sqsQueueName);

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(getQueueUrlResult.getQueueUrl())
                .withMessageBody(hearingResultedJSON)
                .withDelaySeconds(delaySeconds);

        amazonSQS.sendMessage(request);
        log.info("A CP hearing message has been published to the Queue {} with time delay of {} seconds.",sqsQueueName, delaySeconds);
    }
}
