package gov.uk.courtdata.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class MockCdaWebConfig {

    @Bean(name = "cdaOAuth2WebClient")
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
