package gov.uk.courtdata.laaStatus.client;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourtDataAdapterClient {

    @Qualifier("cdaOAuth2WebClient")
    private final WebClient webClient;

    private final GsonBuilder gsonBuilder;

    @Value("${cda.laastatus.url}")
    private String laaUpdateUrl;

    /**
     * @param laaStatusUpdate
     */
    public void postLaaStatus(LaaStatusUpdate laaStatusUpdate) {


        final String laaStatusUpdateJson = gsonBuilder.create().toJson(laaStatusUpdate);
        log.debug("  JSON request : {} ", laaStatusUpdateJson);

        log.info("Post Laa status to CDA.");
        String clientResponse =
                webClient
                        .post()
                        .uri(laaUpdateUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(laaStatusUpdateJson))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();

        log.info("LAA status update posted {}", clientResponse);
    }

}
