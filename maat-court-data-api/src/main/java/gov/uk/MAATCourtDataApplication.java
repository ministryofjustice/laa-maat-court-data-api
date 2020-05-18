package gov.uk;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class MAATCourtDataApplication {

    public static void main(String[] args) {

        SpringApplication.run(MAATCourtDataApplication.class, args);
    }

    @PostConstruct
    public void initSentry() {
        SentryClient sC = Sentry.init();
        //String globalVar = sC.getEnvironment();
    }

    @Bean
    SentryClient sentryCliennt() {
        return SentryClientFactory.sentryClient();
    }
}
