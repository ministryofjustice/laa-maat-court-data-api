package gov.uk.courtdata.integration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

public class OAuth2Stub {

    private static final String OAUTH2_TOKEN_PATH = "/oauth2/token";
    private static final String OAUTH2_TOKEN_URI = "http://localhost:" + MockMvcIntegrationTest.WIREMOCK_PORT + OAUTH2_TOKEN_PATH;

    public String getTokenUri() {
        return OAUTH2_TOKEN_URI;
    }

    public void applyStubTo(WireMockServer wireMockServer){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> token = Map.of(
                "expires_in", 3600,
                "token_type", "Bearer",
                "access_token", UUID.randomUUID()
        );

        wireMockServer.stubFor(
                WireMock.post(OAUTH2_TOKEN_PATH).willReturn(
                        WireMock.ok()
                                .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))
                                .withBody(serialiseOAuth2TokenToJson(mapper, token))
                )
        );
    }

    private String serialiseOAuth2TokenToJson(ObjectMapper mapper, Map<String, Object> oAuthToken) {
        try {
            return mapper.writeValueAsString(oAuthToken);
        } catch (JsonProcessingException e) {
            String message = "Unable to serialise OAuth2 token [%s] due to: [%s]"
                    .formatted(oAuthToken, e.getMessage());
            throw new RuntimeException(message, e);
        }
    }
}
