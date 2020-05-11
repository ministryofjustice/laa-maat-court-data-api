package gov.uk.courtdata.laaStatus.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CourtDataApiClient {
//
//    @Autowired
//    private GsonBuilder gsonBuilder;
//
//    @Value("${cda.oauth.url}")
//    private String tokenURL;
//    @Value("${cda.oauth.clientId}")
//    private String oauthClientId;
//    @Value("${cda.oauth.clientSecret}")
//    private String oauthSecret;
//    @Value("${cda.laastatus.url}")
//    private String laaUpdateUrl;
//
//    @Autowired
//    private RequestInterceptor oauth2FeignRequestInterceptor;
//
//    private static Throwable handleAuthTokenError(Throwable e) {
//        log.error("Exception caught trying to process authentication token. ", e);
//        return null;
//    }
//
//    private static Throwable handleCDAError(Throwable e) {
//        log.error("Exception caught trying to post CDA. ", e);
//        return null;
//    }
//
//    public void invoke(LaaStatusUpdate laaStatusUpdate) {
//
//        log.info("Get oauth token");
//        log.info("JSON=" + gsonBuilder.create().toJson(laaStatusUpdate));
//
//
//        CourtDataAdaptorClient cdaClient = Feign.builder()
//                .client(new OkHttpClient())
//                .encoder(new GsonEncoder())
//                .decoder(new GsonDecoder())
//                .requestInterceptor(oauth2FeignRequestInterceptor)
//                //       .logger(new Slf4jLogger(CourtDataAdaptorClient.class))
//                .logLevel(Logger.Level.FULL)
//                .target(CourtDataAdaptorClient.class, tokenURL);
//
//
//        //    ResponseEntity<Token> oAuthToken = cdaClient.getOAuthToken(oauthClientId, oauthSecret);
//
//        cdaClient.postLaaStatusUpdate(laaStatusUpdate);
//
//
////        laaStatus.block();
//        log.info("After update to LAA status update");
//    }
}
