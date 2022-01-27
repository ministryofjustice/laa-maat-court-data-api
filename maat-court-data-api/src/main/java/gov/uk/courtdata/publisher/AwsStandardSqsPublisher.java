package gov.uk.courtdata.publisher;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import gov.uk.courtdata.config.AmazonSQSConfig;
import gov.uk.courtdata.prosecutionconcluded.listner.request.crowncourt.ProsecutionConcludedRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsStandardSqsPublisher {

    @Value("${cloud-platform.aws.sqs.queue.config.messageDelay}")
    private Integer delaySeconds;

    private final AmazonSQSConfig amazonSQSConfig;

    public void publish(String sqsQueueName, String toJson) {

        log.info("MAAT Record is locked. Publishing a message to the prosecution concluded queue to process later.");
        log.info("Publishing to SQS Queue {} ", sqsQueueName);

        AmazonSQS amazonSQS = amazonSQSConfig.awsSqsClient();
        GetQueueUrlResult getQueueUrlResult = amazonSQS.getQueueUrl(sqsQueueName);

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(getQueueUrlResult.getQueueUrl())
                .withMessageBody(toJson)
                .withDelaySeconds(delaySeconds);

        amazonSQS.sendMessage(request);
        log.info("A CP hearing message has been published to the Queue {} with time delay of {} seconds.",sqsQueueName, delaySeconds);
    }
}
