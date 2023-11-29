package gov.uk.courtdata.config;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.util.Map;

@Component
@ConditionalOnProperty(prefix = "management.metrics.export.cloudwatch", name = "enabled")
public class MicrometerCloudwatchConfig {

    @Bean
    public Clock micrometerClock() {
        return Clock.SYSTEM;
    }

    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient(AwsCloudProperties awsCloudProperties) {
        return CloudWatchAsyncClient.builder().region(Region.of(awsCloudProperties.getRegion())).build();
    }

    @Bean
    public CloudWatchMeterRegistry cloudWatchMeterRegistry(CloudWatchConfig config, Clock clock,
                                                           CloudWatchAsyncClient client) {
        return new CloudWatchMeterRegistry(config, clock, client);
    }

    @Bean
    public CloudWatchConfig cloudWatchConfig(CloudWatchProperties properties) {
        return new CloudWatchConfig() {
            private Map<String, String> configuration
                    = Map.of("cloudwatch.namespace", properties.getNamespace(),
                    "cloudwatch.step", properties.getStep().toString());

            @Override
            public String get(String key) {
                return configuration.get(key);
            }
        };
    }
}

