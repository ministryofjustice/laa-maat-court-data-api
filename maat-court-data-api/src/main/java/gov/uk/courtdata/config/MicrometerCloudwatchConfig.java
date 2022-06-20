package gov.uk.courtdata.config;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.lang.Nullable;
import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.time.Duration;

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
            @Override
            @NonNull
            public String prefix() {
                return "cloudwatch";
            }

            @Override
            @NonNull
            public String namespace() {
                return properties.getNamespace();
            }

            @Override
            @NonNull
            public Duration step() {
                return properties.getStep();
            }

            @Override
            @NonNull
            public boolean enabled() {
                return properties.isEnabled();
            }

            @Override
            @NonNull
            public int batchSize() {
                return properties.getBatchSize();
            }

            @Override
            @Nullable
            public String get(String s) {
                return null;
            }
        };
    }
}

