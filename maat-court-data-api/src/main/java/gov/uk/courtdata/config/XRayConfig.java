package gov.uk.courtdata.config;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@ConditionalOnProperty(prefix = "xray", value = "enabled", havingValue = "true")
public class XRayConfig {
    @Bean
    public Filter TracingFilter() {
        return new AWSXRayServletFilter("maat-api");
    }
}
