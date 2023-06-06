package gov.uk.courtdata.link.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import gov.uk.courtdata.service.QueueMessageLogService;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;

import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import static java.util.List.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestPropertySource(properties = {
        "cloud.aws.sqs.listener.auto-startup = true"
})
public class CreateLinkListenerEndToEndTest extends SqsIntegrationTest {

    private static final int ONCE = 1;

    @MockBean
    private CreateLinkService createLinkService;

    @MockBean
    private QueueMessageLogService queueMessageLogService;

    @Autowired
    private QueueMessagingTemplate sqsTemplate;

    @Autowired
    private AmazonSQSAsync SQS;

    @Value("${cloud-platform.aws.sqs.queue.link}")
    private String queueName;

    private String linkQueueUrl;

    @BeforeEach
    public void setup() {
        linkQueueUrl = SQS.getQueueUrl(queueName).getQueueUrl();
    }

    @Test
    public void givenJSONMessageIsReceived_whenCreateLinkListenerIsInvoked_thenCreateLinkServiceIsCalled() {
        String messageJson = "{\"caseUrn\":\"123\", \"maatId\":4321, \"asn\":null, \"docLanguage\":null, " +
                "\"caseCreationDate\":null, \"cjsAreaCode\":null, \"cjsLocation\":null, \"createdUser\":null, " +
                "\"defendant\":null, \"isActive\":false, \"sessions\":null, \"category\":null, " +
                "\"laaTransactionId\":null, \"onlyForCDAService\":false}";

        sqsTemplate.convertAndSend(queueName, messageJson);

        await().atMost(3, SECONDS).untilAsserted(() -> {
            // assert the SQS is in expected state
            assertThat(numberOfMessagesInLinkQueue()).isEqualTo(0);
            assertThat(numberOfMessagesNotVisibleInLinkQueue()).isEqualTo(0);

            // verify behaviour as expected
            verify(createLinkService, times(ONCE)).saveAndLink(any());
            verify(queueMessageLogService, times(ONCE)).createLog(any(), anyString());
        });

    }

    private Integer numberOfMessagesInLinkQueue() {
        GetQueueAttributesResult attributes = SQS
                .getQueueAttributes(linkQueueUrl, of("All"));

        return Integer.parseInt(
                attributes.getAttributes().get("ApproximateNumberOfMessages")
        );
    }

    private Integer numberOfMessagesNotVisibleInLinkQueue() {
        GetQueueAttributesResult attributes = SQS
                .getQueueAttributes(linkQueueUrl, of("All"));

        return Integer.parseInt(
                attributes.getAttributes().get("ApproximateNumberOfMessagesNotVisible")
        );
    }

}