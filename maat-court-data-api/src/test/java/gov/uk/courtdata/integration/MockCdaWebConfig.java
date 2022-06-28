package gov.uk.courtdata.integration;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class MockCdaWebConfig {

    @Bean(name = "cdaOAuth2WebClient")
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @MockBean(name = "messageListenerContainer")
    private DefaultJmsListenerContainerFactory messageListenerContainer;

    @MockBean(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    @MockBean(name = "sqsConnectionFactory")
    private SQSConnectionFactory sqsConnectionFactory;
}
