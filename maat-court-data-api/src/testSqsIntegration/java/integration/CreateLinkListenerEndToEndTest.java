package integration;


import cloud.localstack.awssdkv1.TestUtils;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.link.service.CreateLinkService;
import gov.uk.courtdata.service.QueueMessageLogService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static java.util.List.of;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;
import static org.testcontainers.shaded.org.awaitility.Awaitility.with;

@Testcontainers
@SpringBootTest(classes = {MAATCourtDataApplication.class})
@AutoConfigureWireMock(port = 9999)
class CreateLinkListenerEndToEndTest {

    private static final int ONCE = 1;

    @MockBean
    private CreateLinkService createLinkService;

    @MockBean
    private QueueMessageLogService queueMessageLogService;


    public static AmazonSQS amazonSQS;

    public static String queueUrl;


    private static DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:2.3.2");

    @Container
    public static LocalStackContainer LOCALSTACK_CONTAINER = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withServices(SQS);

    /**
     * Just configures Localstack's SQS server endpoint in the application
     */
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.cloud.aws.sqs.endpoint",
                () -> LOCALSTACK_CONTAINER.getEndpointOverride(SQS).toString());
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        amazonSQS = TestUtils.getClientSQS(LOCALSTACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.SQS).toString());
        queueUrl = amazonSQS.createQueue("link").getQueueUrl();
    }


    @Test
    void givenJSONMessageIsReceived_whenCreateLinkListenerIsInvoked_thenCreateLinkServiceIsCalled() {
        String messageJson = "{\"caseUrn\":\"123\", \"maatId\":4321, \"asn\":null, \"docLanguage\":null, " +
                "\"caseCreationDate\":null, \"cjsAreaCode\":null, \"cjsLocation\":null, \"createdUser\":null, " +
                "\"defendant\":null, \"isActive\":false, \"sessions\":null, \"category\":null, " +
                "\"laaTransactionId\":null, \"onlyForCDAService\":false}";
        amazonSQS.sendMessage(queueUrl, messageJson);

        with().pollDelay(10, SECONDS).pollInterval(10, SECONDS).await().atMost(60, SECONDS)
                .untilAsserted(() -> {
                    assertThat(numberOfMessagesInLinkQueue()).isEqualTo(0);
                    assertThat(numberOfMessagesNotVisibleInLinkQueue()).isEqualTo(0);

                    // verify behaviour as expected
                    verify(createLinkService, times(ONCE)).saveAndLink(any());
                    verify(queueMessageLogService, times(ONCE)).createLog(any(), anyString());
                });

    }

    private Integer numberOfMessagesInLinkQueue() {
        GetQueueAttributesResult attributes = amazonSQS
                .getQueueAttributes(queueUrl, of("All"));

        return Integer.parseInt(
                attributes.getAttributes().get("ApproximateNumberOfMessages")
        );
    }

    private Integer numberOfMessagesNotVisibleInLinkQueue() {
        GetQueueAttributesResult attributes = amazonSQS.
                getQueueAttributes(queueUrl, of("All"));

        return Integer.parseInt(
                attributes.getAttributes().get("ApproximateNumberOfMessagesNotVisible")
        );
    }

}
