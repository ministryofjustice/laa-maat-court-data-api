package gov.uk.courtdata.config;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.StepRegistryProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "management.metrics.export.cloudwatch")
public class CloudWatchProperties extends StepRegistryProperties {
    private String namespace;
    private Boolean enabled;
    private Duration step;
    private Integer batchSize;
}
