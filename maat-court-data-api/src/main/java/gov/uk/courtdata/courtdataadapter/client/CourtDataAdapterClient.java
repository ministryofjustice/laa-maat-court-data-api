package gov.uk.courtdata.courtdataadapter.client;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.exception.ApiClientException;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.service.QueueMessageLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.UUID;

import static gov.uk.courtdata.constants.CourtDataConstants.CDA_TRANSACTION_ID_HEADER;

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
        ResponseEntity<Void> clientResponse = getApiResponseViaPOST(BodyInserters.fromValue(laaStatusUpdateJson), ResponseEntity.class, courtDataAdapterClientConfig.getLaaStatusUrl(), headers);
        log.info("LAA status update posted {}", clientResponse.getStatusCode());
    }

    public void triggerHearingProcessing(UUID hearingId, String laaTransactionId) {
        log.info("Triggering processing for hearing '{}' via court data adapter.", hearingId);
        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path(courtDataAdapterClientConfig.getHearingUrl()).queryParam("publish_to_queue", true).build(hearingId))
                .headers(httpHeaders -> httpHeaders.setAll(Map.of(CDA_TRANSACTION_ID_HEADER, laaTransactionId)))
                .retrieve().toBodilessEntity()
                .onErrorMap(error -> new MAATCourtDataException(String.format("Error triggering CDA processing for hearing '%s'.%s", hearingId, error.getMessage())))
                .doOnSuccess(response -> log.info("Processing trigger successfully for hearing '{}'", hearingId))
                .block();
    }

    private <T, R> R getApiResponseViaPOST(T postBody, Class<R> responseClass, String url, Map<String, String> headers) {
        return webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path(url).build())
                .headers(httpHeaders -> httpHeaders.setAll(headers))
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(postBody))
                .retrieve()
                .bodyToMono(responseClass)
                .onErrorMap(this::handleError)
                .block();
    }

    private Throwable handleError(Throwable error) {
        if (error instanceof ApiClientException) {
            return error;
        }
        return new ApiClientException("Call to Court Data Adapter failed, invalid response.", error);
    }
}
