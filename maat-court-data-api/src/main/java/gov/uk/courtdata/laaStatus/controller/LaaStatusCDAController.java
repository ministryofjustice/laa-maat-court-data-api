package gov.uk.courtdata.laaStatus.controller;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.model.Token;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;


@Slf4j
@RequiredArgsConstructor
@Service
public class LaaStatusCDAController {

    @Autowired
    private final GsonBuilder gsonBuilder;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${cda.oauth.url}")
    private String tokenURL;
    @Value("${cda.oauth.clientId}")
    private String oauthClientId;
    @Value("${cda.oauth.clientSecret}")
    private String oauthSecret;
    @Value("${cda.laastatus.url}")
    private String laaUpdateUrl;

    public void updateLaaStatus(LaaStatusUpdate laaStatusUpdate) {

        gsonBuilder.serializeNulls();

        log.info("Get oauth token");
        log.info("JSON=" + gsonBuilder.create().toJson(laaStatusUpdate));

        //Generate Token
        URI oauthURI = UriComponentsBuilder
                .fromUriString(tokenURL)
                .queryParam("client_id", oauthClientId)
                .queryParam("client_secret", oauthSecret)
                .build().toUri();

        HttpHeaders header = new HttpHeaders();
        header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        header.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LaaStatusUpdate> oauthRequest =
                new HttpEntity<>(null, header);

        log.info("Oauth URL==" + oauthURI.toString());

        ResponseEntity<Token> token = restTemplate.exchange(oauthURI, HttpMethod.POST, HttpEntity.EMPTY, Token.class);

        log.info("Token=" + token.toString());

        //Invoke CP laa update
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token.getBody().getAccess_token());

        HttpEntity<LaaStatusUpdate> requestEntity =
                new HttpEntity<>(laaStatusUpdate, headers);

        log.info("laa update URL:" + laaUpdateUrl);
        ResponseEntity<LaaStatusUpdate> laaStatusResp
                = restTemplate.exchange(laaUpdateUrl, HttpMethod.POST, requestEntity, LaaStatusUpdate.class);
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


}
