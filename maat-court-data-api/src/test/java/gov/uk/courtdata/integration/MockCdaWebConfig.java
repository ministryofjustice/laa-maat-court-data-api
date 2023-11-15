package gov.uk.courtdata.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class MockCdaWebConfig {

    @Bean(name = "cdaOAuth2WebClient")
    public WebClient webClient(@Value("${cda.url}") String baseUrl) {
        return WebClient.builder().baseUrl(baseUrl).build();
    }

}
