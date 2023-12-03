package gov.uk.courtdata.courtdataadapter.client;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.service.QueueMessageLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import uk.gov.justice.laa.crime.commons.client.RestAPIClient;

import java.util.Map;
import java.util.UUID;

import static gov.uk.courtdata.constants.CourtDataConstants.CDA_TRANSACTION_ID_HEADER;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourtDataAdapterClient {

    @Qualifier("cdaApiClient")
    private final RestAPIClient cdaAPIClient;

    private final GsonBuilder gsonBuilder;

    private final QueueMessageLogService queueMessageLogService;

    private final CourtDataAdapterClientConfig courtDataAdapterClientConfig;

    public void postLaaStatus(LaaStatusUpdate laaStatusUpdate, Map<String,String> headers) {
        final String laaStatusUpdateJson = gsonBuilder.create().toJson(laaStatusUpdate);
        queueMessageLogService.createLog(MessageType.LAA_STATUS_UPDATE,laaStatusUpdateJson);
        log.info("Post Laa status to CDA.");
        cdaAPIClient.post(
                laaStatusUpdateJson,
                new ParameterizedTypeReference<>() {},
                courtDataAdapterClientConfig.getLaaStatusUrl(),
                headers
        );
        log.info("LAA status update posted");
    }

    public void triggerHearingProcessing(UUID hearingId, String laaTransactionId) {
        log.info("Triggering processing for hearing '{}' via court data adapter.", hearingId);
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("publish_to_queue", "true");
        cdaAPIClient.get(
                new ParameterizedTypeReference<Void>() {},
                courtDataAdapterClientConfig.getHearingUrl(),
                Map.of(CDA_TRANSACTION_ID_HEADER, laaTransactionId),
                queryParams,
                hearingId
        );
    }

}
