package gov.uk.courtdata.laaStatus.controller;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import gov.uk.courtdata.model.Token;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.model.laastatus.RepOrderData;
import gov.uk.courtdata.model.laastatus.RootData;
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

    public void updateLaaStatus(RootData repOrderData) {

        log.info("Get oauth token");

        final String json = gsonBuilder.create().toJson(repOrderData);
        log.info("JSON=" + json);

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

        HttpEntity<String> requestEntity =
                new HttpEntity<>(json, headers);

        log.info("laa update URL:" + laaUpdateUrl);
        ResponseEntity<String> laaStatusResp
                = restTemplate.exchange(laaUpdateUrl, HttpMethod.POST, requestEntity,String.class);
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }


}
