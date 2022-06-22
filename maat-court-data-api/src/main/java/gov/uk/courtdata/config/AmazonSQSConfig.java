package gov.uk.courtdata.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import gov.uk.courtdata.jms.SqsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonSQSConfig {

    private final SqsProperties sqsProperties;

    public AmazonSQS awsSqsClient() {
        return AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(sqsProperties.getAccesskey(), sqsProperties.getSecretkey())))
                .withRegion(Regions.fromName(sqsProperties.getRegion()))
                .build();
    }
}
