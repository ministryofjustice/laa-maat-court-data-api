package gov.uk.courtdata.courtdataadapter.client;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.service.QueueMessageLogService;
import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourtDataAdapterClient {

    @Qualifier("cdaOAuth2WebClient")
    private final WebClient webClient;

    private final GsonBuilder gsonBuilder;

    private final QueueMessageLogService queueMessageLogService;

    private final CourtDataAdapterClientConfig courtDataAdapterClientConfig;

    /**
     * @param laaStatusUpdate laa status value
     */
    public void postLaaStatus(LaaStatusUpdate laaStatusUpdate, Map<String,String> headers) {


        final String laaStatusUpdateJson = gsonBuilder.create().toJson(laaStatusUpdate);
        queueMessageLogService.createLog(MessageType.LAA_STATUS_UPDATE,laaStatusUpdateJson);

        log.info("Post Laa status to CDA.");
        WebClient.ResponseSpec clientResponse =
                webClient
                        .post()
                        .uri(courtDataAdapterClientConfig.getLaaStatusUrl())
                        .headers(httpHeaders -> httpHeaders.setAll(headers))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(laaStatusUpdateJson))
                        .retrieve();

        log.info("LAA status update posted {}", Optional.of( clientResponse.toBodilessEntity().block().getStatusCode() ));
    }

    public void triggerHearingProcessing(UUID hearingId, String laaTransactionId) {
        log.info("Triggering processing for hearing '{}' via court data adapter.", hearingId);
        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(courtDataAdapterClientConfig.getHearingUrl()).queryParam("publish_to_queue", true).build(hearingId))
                .headers(httpHeaders -> httpHeaders.setAll(Map.of("X-Request-ID", laaTransactionId)))
                .retrieve().toBodilessEntity()
                .doOnError(Sentry::captureException)
                .doOnSuccess(response -> log.info("Processing trigger successfully for hearing '{}'", hearingId))
                .block();
    }
}
