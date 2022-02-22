package gov.uk.courtdata.publisher;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import gov.uk.courtdata.config.AmazonSQSConfig;
import gov.uk.courtdata.exception.MaatRecordLockedException;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.service.QueueMessageLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsStandardSqsPublisher {

    @Value("${cloud-platform.aws.sqs.queue.config.messageDelay}")
    private Integer delaySeconds;

    @Value("${cloud-platform.aws.sqs.queue.config.messageDelayDuration}")
    private Integer messageDelayDuration;


    @Value("${cloud-platform.aws.sqs.queue.prosecutionConcluded}")
    private String sqsQueueName = "";

    private final Gson gson;


    private final AmazonSQSConfig amazonSQSConfig;

    private final QueueMessageLogService queueMessageLogService;

    private void publish(String toJson, Integer messageDelayDuration) {


        log.info("Publishing to SQS Queue {} ", sqsQueueName);

        AmazonSQS amazonSQS = amazonSQSConfig.awsSqsClient();
        GetQueueUrlResult getQueueUrlResult = amazonSQS.getQueueUrl(sqsQueueName);

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(getQueueUrlResult.getQueueUrl())
                .withMessageBody(toJson)
                .withDelaySeconds(messageDelayDuration);

        amazonSQS.sendMessage(request);
        log.info("A CP hearing message has been published to the Queue {} with time delay of {} seconds.",sqsQueueName, messageDelayDuration);
    }


    public void publishMessageToProsecutionSQS(ProsecutionConcluded prosecutionConcluded) {

        log.info("MAAT Record is locked. Publishing a message to the prosecution concluded queue to process later.");
        log.info("Message retry attempt no. " + prosecutionConcluded.getMessageRetryCounter());

        if (prosecutionConcluded.getMessageRetryCounter() < 6) {
            log.info("Publishing a message to the SQS again, with retry number {}", prosecutionConcluded.getMessageRetryCounter());

            int counter = prosecutionConcluded.getMessageRetryCounter()+1;
            prosecutionConcluded.setMessageRetryCounter(counter);
            String toJson = gson.toJson(prosecutionConcluded);

             publish(toJson, delaySeconds);
        } else {
            throw new MaatRecordLockedException("Unable to process CP hearing notification because Maat Record is locked.");
        }
    }

    public void publishingSqsMessageForHearing(ProsecutionConcluded prosecutionConcluded) {

        log.info("Hearing data not available for hearing-id {} - publishing message back to the SQS {} - Hearing", prosecutionConcluded.getHearingIdWhereChangeOccurred() ,sqsQueueName);
        int counter = queueMessageLogService.getMessageCounterByMaatId(prosecutionConcluded.getMaatId());
        if (counter < 6 ) {
            log.info("Publishing a message to the SQS again, with retry number {}", counter);
            String toJson = gson.toJson(prosecutionConcluded);
            publish(toJson, messageDelayDuration);
        } else {
            log.info("A message retried multiple times and breaking the republishing sqs chain {}", prosecutionConcluded.getMessageRetryCounter());
            throw new MaatRecordLockedException("Hearing data not available for this maat and throwing the exception.");
        }
    }
}