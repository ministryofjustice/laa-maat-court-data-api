package gov.uk.courtdata.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CDAClientConfig {

//    @Value("${cda.oauth.url}")
//    private String accessTokenUri;
//    @Value("${cda.oauth.clientId}")
//    private String clientId;
//    @Value("${cda.oauth.clientSecret}")
//    private String clientSecret;
//
//    @Bean("oauth2FeignRequestInterceptor")
//    public RequestInterceptor oauth2FeignRequestInterceptor() {
//        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), resource());
//    }
//
//    @Bean
//    Logger.Level feignLoggerLevel() {
//        return Logger.Level.FULL;
//    }
//
//
//    private OAuth2ProtectedResourceDetails resource() {
//        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
//        resourceDetails.setAccessTokenUri(accessTokenUri);
//        resourceDetails.setClientId(clientId);
//        resourceDetails.setClientSecret(clientSecret);
//        return resourceDetails;
//    }

}

