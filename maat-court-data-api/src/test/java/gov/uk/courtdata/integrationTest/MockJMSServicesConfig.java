package gov.uk.courtdata.integrationTest;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import gov.uk.courtdata.config.SentryConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@TestConfiguration
public class MockJMSServicesConfig {

    @MockBean(name = "messageListenerContainer")
    private DefaultJmsListenerContainerFactory messageListenerContainer;

    @MockBean(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    @MockBean(name = "sqsConnectionFactory")
    private SQSConnectionFactory sqsConnectionFactory;

    @MockBean(name = "sentryConfig")
    private SentryConfig sentryConfig;

}
