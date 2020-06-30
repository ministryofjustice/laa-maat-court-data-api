package gov.uk.courtdata.hearing.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;
import gov.uk.courtdata.config.AmazonSQSConfig;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.hearing.validator.HearingValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;

@RunWith(MockitoJUnitRunner.class)
public class HearingResultedPublisherTest {

    @InjectMocks
    private HearingResultedPublisher hearingResultedPublisher;

    @Mock
    private HearingValidationProcessor hearingValidationProcessor;

    @Mock
    Gson gson;

    @Mock
    AmazonSQSConfig amazonSQSConfig;

    @Mock
    AmazonSQS amazonSqs;

    @Mock
    GetQueueUrlResult getQueueUrlResult;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_publishMessage_backup () {

        HearingResulted hearingDetails = HearingResulted.builder()
                .jurisdictionType(JurisdictionType.CROWN)
                .messageRetryCounter(10)
                .maatId(123456)
                .build();
       final String queueName = "queue";
        //SendMessageRequest messageRequest = new SendMessageRequest("queue-url", "");
        Mockito.when(amazonSqs.getQueueUrl(queueName)).thenReturn(getQueueUrlResult);
        Mockito.when(amazonSQSConfig.awsSqsClient()).thenReturn(amazonSqs);

        //GetQueueUrlResult getQueueUrlResult = new GetQueueUrlResult();
        //getQueueUrlResult.setQueueUrl(queueName);
        //getQueueUrlResult.setQueueUrl(queueName);
        amazonSqs.setQueueAttributes(queueName, new HashMap<>());

        //Mockito.when(getQueueUrlResult.getQueueUrl()).thenReturn(queueName);


        SendMessageResult sendMessageResult = new SendMessageResult();
        Mockito.when(amazonSqs.sendMessage(Mockito.any())).thenReturn(sendMessageResult);
        hearingResultedPublisher.publish(hearingDetails);

        //then
      //  verify(hearingResultedPublisher).publish(hearingDetails);



    }



    private String generateStringWithLength(int messageLength) {
        char[] charArray = new char[messageLength];
        Arrays.fill(charArray, 'x');
        return new String(charArray);
    }
}
