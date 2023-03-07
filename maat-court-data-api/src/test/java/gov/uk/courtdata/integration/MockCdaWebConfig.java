package gov.uk.courtdata.integration;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import io.awspring.cloud.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class MockCdaWebConfig {

    @Bean(name = "cdaOAuth2WebClient")
    public WebClient webClient(@Value("${cda.url}") String baseUrl) {
        return WebClient.builder().baseUrl(baseUrl).build();
    }

    @MockBean(name = "messageListenerContainer")
    private DefaultJmsListenerContainerFactory messageListenerContainer;

    @MockBean(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    @MockBean(name = "sqsConnectionFactory")
    private SQSConnectionFactory sqsConnectionFactory;

    @MockBean(name = "cmaSQSConnectionFactory")
    private SQSConnectionFactory cmaSQSConnectionFactory;

    @MockBean(name = "simpleMessageListenerContainer")
    private SimpleMessageListenerContainer simpleMessageListenerContainer;
}
