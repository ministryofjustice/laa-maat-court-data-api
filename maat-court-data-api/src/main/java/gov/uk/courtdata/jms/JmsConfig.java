package gov.uk.courtdata.jms;


import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import gov.uk.courtdata.config.AmazonSQSConfig;
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
 * <Class>JmsConfig</Class> for SQS hosted on clouds platform with .
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableJms
public class JmsConfig {

    private final JmsErrorHandler jmsErrorHandler;

    private final SqsProperties sqsProperties;

    private final AmazonSQSConfig amazonSQSConfig;

    /**
     * Use the default container configured.
     *
     * @return
     *
     */

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {

        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(sqsConnectionFactory());
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setConcurrency("1-3");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setErrorHandler(jmsErrorHandler);
        return factory;

    }

    /**
     * Create the jms template with provider config for the SQS client.
     *
     * @return
     */
    @Bean
    public JmsTemplate defaultJmsTemplate() {
        return new
                JmsTemplate(sqsConnectionFactory());
    }


    @Bean
    public SQSConnectionFactory sqsConnectionFactory() {

        return new SQSConnectionFactory(new ProviderConfiguration(),
                amazonSQSConfig.awsSqsClient());
    }
}


