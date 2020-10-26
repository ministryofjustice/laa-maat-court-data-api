package gov.uk.courtdata.config;

import io.sentry.Sentry;


import io.sentry.SentryClient;
import io.sentry.SentryOptions;
import io.sentry.config.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Semaphore;


@Component
public class SentryConfig {

    @Value("${sentry.environment}")
    private String env;

    @Value("${sentry.release}")
    private String release;

    @Value("${sentry.servername}")
    private String serverName;





    @PostConstruct
    public void initSentry() {





    }

//    @Bean
//    SentryClient sentryCliennt() {
//        return SentryClientFactory.sentryClient();
//    }
}
