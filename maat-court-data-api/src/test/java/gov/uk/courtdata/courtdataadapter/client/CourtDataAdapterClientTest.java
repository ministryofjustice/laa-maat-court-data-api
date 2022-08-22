package gov.uk.courtdata.courtdataadapter.client;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.model.laastatus.RepOrderData;
import gov.uk.courtdata.service.QueueMessageLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtDataAdapterClientTest {

    private final String hearingUrl = "cda-test/hearing/{hearingId}";

    @Mock
    private ExchangeFunction shortCircuitExchangeFunction;
    @Mock
    private CourtDataAdapterClientConfig courtDataAdapterClientConfig;
    @Mock
    private QueueMessageLogService queueMessageLogService;
    @Captor
    ArgumentCaptor<ClientRequest> requestCaptor;

    private GsonBuilder gsonBuilder;
    private CourtDataAdapterClient courtDataAdapterClient;

    @BeforeEach
    public void setup() {
        WebClient testWebClient = WebClient
                .builder()
                .exchangeFunction(shortCircuitExchangeFunction)
                .build();

        gsonBuilder = new GsonBuilder();

        courtDataAdapterClient = new CourtDataAdapterClient(testWebClient, gsonBuilder, queueMessageLogService, courtDataAdapterClientConfig);
    }

    @Test
    public void givenAValidLaaStatusObject_whenPostLaaStatusIsInvoked_thenTheRequestIsSentCorrectly() {
        when(shortCircuitExchangeFunction.exchange(requestCaptor.capture())).thenReturn(Mono.just(ClientResponse.create(HttpStatus.OK).build()));
        String laaStatusUrl = "cda-test/laaStatus";
        when(courtDataAdapterClientConfig.getLaaStatusUrl()).thenReturn(laaStatusUrl);
        Map<String, String> headers = Map.of("test-header", "test-header-value");
        LaaStatusUpdate testStatusObject = getTestLaaStatusObject();

        courtDataAdapterClient.postLaaStatus(testStatusObject, headers);
        String jsonBody = gsonBuilder.create().toJson(testStatusObject);
        verify(queueMessageLogService, atLeastOnce())
                .createLog(MessageType.LAA_STATUS_UPDATE, jsonBody);

        Map<String, String> expectedFinalHeaders = Map.of(
                "test-header", "test-header-value","Content-Type", "application/json");
        validateRequest(requestCaptor.getValue(), laaStatusUrl, expectedFinalHeaders, HttpMethod.POST);
    }

    @Test
    public void givenAValidHearingId_whenTriggerHearingProcessingIsInvoked_thenTheRequestIsSentCorrectly() {
        when(shortCircuitExchangeFunction.exchange(requestCaptor.capture()))
                .thenReturn(Mono.just(ClientResponse.create(HttpStatus.OK).build()));
        when(courtDataAdapterClientConfig.getHearingUrl()).thenReturn(hearingUrl);

        UUID testHearingId = UUID.randomUUID();
        String testTransactionId = UUID.randomUUID().toString();
        courtDataAdapterClient.triggerHearingProcessing(testHearingId, testTransactionId);

        validateTriggerHearingProcessingScenario(testHearingId, testTransactionId);
    }

    @Test
    public void givenAValidHearingId_whenTriggerHearingProcessingIsInvokedAndTheCallFails_thenFailureIsHandled() {
        when(shortCircuitExchangeFunction.exchange(requestCaptor.capture()))
                .thenReturn(Mono.just(ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR).build()));

        when(courtDataAdapterClientConfig.getHearingUrl()).thenReturn(hearingUrl);

        UUID testHearingId = UUID.randomUUID();
        String testTransactionId = UUID.randomUUID().toString();
        MAATCourtDataException error = assertThrows(MAATCourtDataException.class, () -> courtDataAdapterClient.triggerHearingProcessing(testHearingId, testTransactionId));

        assertTrue(error.getMessage().contains(String.format("Error triggering CDA processing for hearing '%s'.", testHearingId)));
        validateTriggerHearingProcessingScenario(testHearingId, testTransactionId);
    }

    private void validateTriggerHearingProcessingScenario(UUID testHearingId, String laaTransactionId) {
        Map<String, String> expectedFinalHeaders = Map.of("X-Request-ID", laaTransactionId);
        String expectedUrl = String.format("%s?publish_to_queue=true", hearingUrl);
        expectedUrl = expectedUrl.replace("{hearingId}", testHearingId.toString());
        validateRequest(requestCaptor.getValue(), expectedUrl, expectedFinalHeaders, HttpMethod.GET);
    }

    private void validateRequest(ClientRequest request, String expectedUrl, Map<String, String> expectedHeaders, HttpMethod method) {
        assertEquals(request.headers().toSingleValueMap(), expectedHeaders);
        assertEquals(request.url().toString(), expectedUrl);
        assertEquals(request.method(), method);
    }

    private LaaStatusUpdate getTestLaaStatusObject() {
        return LaaStatusUpdate.builder().data(RepOrderData.builder().type("test-representation_order").build()).build();
    }

}
