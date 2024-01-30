package gov.uk.courtdata.integration.util;

import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IntegrationTestPropertySource extends MapPropertySource {

    private static final Map<String, Object> SPRING_PROPERTIES_MAP =
            Map.of("spring.security.oauth2.client.provider.cda.token-uri", new OAuth2Stub().getTokenUri(),
                    "cda.url", "http://localhost:" + MockMvcIntegrationTest.WIREMOCK_PORT);

    public IntegrationTestPropertySource() {
        super(IntegrationTestPropertySource.class.getSimpleName(), SPRING_PROPERTIES_MAP);
    }
}
