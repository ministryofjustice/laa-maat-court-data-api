package gov.uk.courtdata.jms;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "cloud-platform.aws.sqs")
public class SqsProperties {

    private String region;
    private String accesskey;
    private String secretkey;
    private String cmaAccesskey;
    private String cmaSecretkey;
}
