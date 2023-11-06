package gov.uk.courtdata.integration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class MockServicesConfig {

    @MockBean(name = "clientRegistrationRepository")
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockBean(name = "authorizedClientManager")
    private OAuth2AuthorizedClientManager authorizedClientManager;

    @MockBean(name = "cdaOAuth2WebClient")
    private WebClient cdaOAuth2WebClient;

}
