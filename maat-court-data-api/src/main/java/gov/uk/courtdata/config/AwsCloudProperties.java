package gov.uk.courtdata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws")
public class AwsCloudProperties {
    private String region;
    private boolean xrayEnabled;
}
