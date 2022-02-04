package gov.uk.courtdata.publisher;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import gov.uk.courtdata.config.AmazonSQSConfig;
import gov.uk.courtdata.exception.MaatRecordLockedException;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
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

    @Value("${cloud-platform.aws.sqs.queue.prosecutionConcluded}")
    private String sqsQueueName = "";

    private final Gson gson;


    private final AmazonSQSConfig amazonSQSConfig;

    public void publish(String toJson) {

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


    public void publishMessageToProsecutionSQS(ProsecutionConcluded prosecutionConcluded) {

        log.info("Message retry attempt no. " + prosecutionConcluded.getMessageRetryCounter());

        if (prosecutionConcluded.getMessageRetryCounter() < 6) {
            log.info("Publishing a message to the SQS again, with retry number {}", prosecutionConcluded.getMessageRetryCounter());

            int counter = prosecutionConcluded.getMessageRetryCounter()+1;
            prosecutionConcluded.setMessageRetryCounter(counter);
            String toJson = gson.toJson(prosecutionConcluded);

             publish(toJson);
        } else {
            throw new MaatRecordLockedException("Unable to process CP hearing notification because Maat Record is locked.");
        }
    }
}
