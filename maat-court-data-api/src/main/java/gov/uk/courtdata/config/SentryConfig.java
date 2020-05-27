package gov.uk.courtdata.config;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SentryConfig {

    @Value("${sentry.dsn}")
    private String dsn;

    @Value("${sentry.environment}")
    private String env;

    @Value("${sentry.release}")
    private String release;

    @Value("${sentry.servername}")
    private String serverName;

    @PostConstruct
    public void initSentry() {

        SentryClient sentryClient = Sentry.init(dsn);
        sentryClient.setEnvironment(env);
        sentryClient.setRelease(release);
        sentryClient.setServerName(serverName);
    }

    @Bean
    SentryClient sentryCliennt() {
        return SentryClientFactory.sentryClient();
    }


}
