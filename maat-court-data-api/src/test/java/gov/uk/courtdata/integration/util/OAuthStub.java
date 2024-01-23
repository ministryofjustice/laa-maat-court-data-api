package gov.uk.courtdata.integration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.MediaType;

import java.util.Map;
import java.util.UUID;

public class OAuthStub {

    public void applyStubTo(WireMockServer wireMockServer){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> token = Map.of(
                "expires_in", 3600,
                "token_type", "Bearer",
                "access_token", UUID.randomUUID()
        );

        wireMockServer.stubFor(
                WireMock.post("/oauth2/token").willReturn(
                        WireMock.ok()
                                .withHeader("Content-Type", String.valueOf(MediaType.APPLICATION_JSON))
                                .withBody(serialiseOAuthTokenToJson(mapper, token))
                )
        );
    }

    private String serialiseOAuthTokenToJson(ObjectMapper mapper, Map<String, Object> oAuthToken) {
        try {
            return mapper.writeValueAsString(oAuthToken);
        } catch (JsonProcessingException e) {
            String message = "Unable to serialise OAuth token [%s] due to: [%s]"
                    .formatted(oAuthToken, e.getMessage());
            throw new RuntimeException(message, e);
        }
    }
}
