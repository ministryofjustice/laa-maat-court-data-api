package gov.uk.courtdata.laaStatus.service;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.model.Token;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactivefeign.webclient.WebReactiveFeign;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class CourtDataApiClient {

    @Autowired
    private GsonBuilder gsonBuilder;
    @Value("${cda.oauth.url}")
    private String tokenURL;
    @Value("${cda.oauth.clientId}")
    private String oauthClientId;
    @Value("${cda.oauth.clientSecret}")
    private String oauthSecret;
    @Value("${cda.laastatus.url}")
    private String laaUpdateUrl;

    public void invoke(LaaStatusUpdate laaStatusUpdate) {

        log.info("Get oauth token");
        CourtDataApiService client =
                WebReactiveFeign
                        .<CourtDataApiService>builder()
                        .target(CourtDataApiService.class, tokenURL);

        Mono<Token> oAuthToken = client.getOAuthToken(oauthClientId, oauthSecret)
                .onErrorMap(CourtDataApiClient::handleAuthTokenError);


        Token token = oAuthToken.block();

        Mono<Void>  laaStatus = client.postLaaStatusUpdate(laaStatusUpdate)
                .onErrorMap(CourtDataApiClient::handleCDAError);

        laaStatus.block();
        log.info("After update to LAA status update");
    }

    private static Throwable handleAuthTokenError(Throwable e) {
        log.error("Exception caught trying to process authentication token. ",e);
        return null;
    }

    private static Throwable handleCDAError(Throwable e) {
        log.error("Exception caught trying to post CDA. ",e);
        return null;
    }
}
