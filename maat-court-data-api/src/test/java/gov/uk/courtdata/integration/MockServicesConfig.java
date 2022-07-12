package gov.uk.courtdata.integration;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class MockServicesConfig {

    @MockBean(name = "messageListenerContainer")
    private DefaultJmsListenerContainerFactory messageListenerContainer;

    @MockBean(name = "jmsTemplate")
    private JmsTemplate jmsTemplate;

    @MockBean(name = "sqsConnectionFactory")
    private SQSConnectionFactory sqsConnectionFactory;

    @MockBean(name = "clientRegistrationRepository")
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockBean(name = "authorizedClientManager")
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @MockBean(name = "cdaOAuth2WebClient")
    private WebClient cdaOAuth2WebClient;

    @MockBean(name = "cmaSQSConnectionFactory")
    private SQSConnectionFactory cmaSQSConnectionFactory;
}
