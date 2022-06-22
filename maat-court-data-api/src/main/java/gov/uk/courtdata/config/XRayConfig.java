package gov.uk.courtdata.config;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.slf4j.SLF4JSegmentListener;
import com.amazonaws.xray.strategy.DefaultStreamingStrategy;
import com.amazonaws.xray.strategy.LogErrorContextMissingStrategy;
import com.amazonaws.xray.strategy.sampling.AllSamplingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "xray", value = "enabled", havingValue = "true")
public class XRayConfig {

    @Value("${spring.application.name}")
    private String AWS_XRAY_SEGMENT_NAME;

    static {
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard()
                .withSamplingStrategy(new AllSamplingStrategy())
                .withSegmentListener(new SLF4JSegmentListener())
                .withStreamingStrategy(new DefaultStreamingStrategy(3))
                .withContextMissingStrategy(new LogErrorContextMissingStrategy());

        AWSXRay.setGlobalRecorder(builder.build());
        log.debug("aws xray recorder setup complete.");
    }

    @Bean
    public Filter TracingFilter() {
        return new AWSXRayServletFilter(AWS_XRAY_SEGMENT_NAME);
    }
}
