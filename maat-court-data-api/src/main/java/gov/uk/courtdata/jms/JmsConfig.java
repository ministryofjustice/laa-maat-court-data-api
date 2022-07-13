package gov.uk.courtdata.jms;


import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import gov.uk.courtdata.config.CDASQSConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;


/**
 * <Class>JmsConfig</Class> for SQS hosted on cloud's platform with .
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableJms
public class JmsConfig {

    private final JmsErrorHandler jmsErrorHandler;

    private final CDASQSConfig cdaSQSConfig;

    /**
     * Use the default container configured.
     *
     * @return DefaultJmsListenerContainerFactory
     *
     */

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {

        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(sqsConnectionFactory());
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("1");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setErrorHandler(jmsErrorHandler);
        return factory;

    }

    /**
     * Create the jms template with provider config for the SQS client.
     *
     * @return JmsTemplate
     */
    @Bean
    public JmsTemplate defaultJmsTemplate() {
        return new
                JmsTemplate(sqsConnectionFactory());
    }


    @Bean
    public SQSConnectionFactory sqsConnectionFactory() {

        return new SQSConnectionFactory(new ProviderConfiguration(),
                cdaSQSConfig.awsSqsClient());
    }
}


