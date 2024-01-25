package gov.uk.courtdata.integration.util;

import org.springframework.core.env.MapPropertySource;

import java.util.Map;

public class OAuth2TokenUriPropertySource extends MapPropertySource {

    private static final Map<String, Object> SPRING_PROPERTIES_MAP =
            Map.of("spring.security.oauth2.client.provider.cda.token-uri", new OAuth2Stub().getTokenUri());

    public OAuth2TokenUriPropertySource() {
        super(OAuth2TokenUriPropertySource.class.getSimpleName(), SPRING_PROPERTIES_MAP);
    }
}
