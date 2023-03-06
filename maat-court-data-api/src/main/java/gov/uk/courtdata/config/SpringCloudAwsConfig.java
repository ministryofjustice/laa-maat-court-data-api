package gov.uk.courtdata.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import gov.uk.courtdata.jms.SqsProperties;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
@RequiredArgsConstructor
public class SpringCloudAwsConfig {
    private final SqsProperties sqsProperties;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQS) {
        return new QueueMessagingTemplate(amazonSQS);
    }
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder
                .standard()
                .withRegion(Regions.fromName(sqsProperties.getRegion()))
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(sqsProperties.getAccesskey(), sqsProperties.getSecretkey())
                        )
                )
                .build();
    }
}