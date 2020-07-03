package gov.uk.courtdata.hearing.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import gov.uk.courtdata.config.AmazonSQSConfig;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.model.hearing.HearingResulted;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource(locations = { "classpath:application.yaml" })
public class HearingResultedPublisherTest {

    @InjectMocks
    private HearingResultedPublisher hearingResultedPublisher;

    @Mock
    private Gson gson;

    @Mock
    private AmazonSQSConfig amazonSQSConfig;

    @Mock
    private GetQueueUrlResult getQueueUrlResult;

    @Mock
    AmazonSQS amazonSqs;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void given_hearingPayload_thenPublishMessageToSQS () {

        //given
        HearingResulted hearingDetails = HearingResulted.builder()
                .jurisdictionType(JurisdictionType.CROWN)
                .messageRetryCounter(1)
                .maatId(123456)
                .build();
        //when
        when(amazonSQSConfig.awsSqsClient()).thenReturn(amazonSqs);
        when(amazonSqs.getQueueUrl(anyString())).thenReturn(getQueueUrlResult);



        hearingResultedPublisher.publish(hearingDetails);

        //verify
        verify(amazonSQSConfig).awsSqsClient();
        verify(gson).toJson(any(HearingResulted.class));
        verify(amazonSqs).sendMessage(any(SendMessageRequest.class));
    }
}
