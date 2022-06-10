package gov.uk.courtdata.config;

import org.springframework.cloud.aws.autoconfigure.metrics.CloudWatchExportAutoConfiguration;
import org.springframework.cloud.aws.context.annotation.ConditionalOnAwsCloudEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnAwsCloudEnvironment
public class AwsCloudWatchConfig {

    @Bean
    public CloudWatchExportAutoConfiguration cloudwatchAutoConfiguration() {
        return new CloudWatchExportAutoConfiguration();
    }
}
