package gov.uk.courtdata.courtdataadapter.client;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.model.laastatus.RepOrderData;
import gov.uk.courtdata.service.QueueMessageLogService;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtDataAdapterClientIntegrationTest {

    public static MockWebServer mockCourtDataAdapterApi;

    private String baseUrl;

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
    private WebClient webClient;

    @BeforeEach
    public void setup() throws IOException {
        gsonBuilder = new GsonBuilder();
        mockCourtDataAdapterApi = new MockWebServer();
        mockCourtDataAdapterApi.start();
        baseUrl = String.format("http://localhost:%s", mockCourtDataAdapterApi.getPort());

        webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeFunction(shortCircuitExchangeFunction)
                .filter(CourtDataAdapterOAuth2ClientConfig.retryFilter(3,5, 0.75))
                .filter(CourtDataAdapterOAuth2ClientConfig.errorResponse())
                .build();
    }

    @Test
    public void testBlah() {
        courtDataAdapterClient = new CourtDataAdapterClient(webClient, gsonBuilder, queueMessageLogService, courtDataAdapterClientConfig);
        String laaStatusUrl = "cda-test/laaStatus";
        when(courtDataAdapterClientConfig.getLaaStatusUrl()).thenReturn(laaStatusUrl);

        ClientResponse errorResponse = ClientResponse.create(HttpStatus.INTERNAL_SERVER_ERROR).build();
        ClientResponse successResponse = ClientResponse.create(HttpStatus.ACCEPTED).build();

        LinkedList<Object> responses = Stream.of(errorResponse, successResponse)
                .collect(Collectors.toCollection(LinkedList::new));

        when(shortCircuitExchangeFunction.exchange(requestCaptor.capture()))
                .thenReturn(Mono.just((ClientResponse) responses.pop()));

        Map<String, String> headers = Map.of("test-header", "test-header-value");
        LaaStatusUpdate testStatusObject = getTestLaaStatusObject();

        courtDataAdapterClient.postLaaStatus(testStatusObject, headers);

        String jsonBody = gsonBuilder.create().toJson(testStatusObject);

        verify(queueMessageLogService, atLeastOnce())
                .createLog(MessageType.LAA_STATUS_UPDATE, jsonBody);

        verify(shortCircuitExchangeFunction, times(2)).exchange(any());

        Map<String, String> expectedFinalHeaders = Map.of(
                "test-header", "test-header-value","Content-Type", "application/json");
        validateRequest(requestCaptor.getValue(), laaStatusUrl, expectedFinalHeaders, HttpMethod.POST);
    }

    private void validateRequest(ClientRequest request, String expectedUrl, Map<String, String> expectedHeaders, HttpMethod method) {
        assertEquals(request.headers().toSingleValueMap(), expectedHeaders);
        assertEquals(request.url().toString(), String.format("%s%s", baseUrl, expectedUrl));
        assertEquals(request.method(), method);
    }

    private LaaStatusUpdate getTestLaaStatusObject() {
        return LaaStatusUpdate.builder().data(RepOrderData.builder().type("test-representation_order").build()).build();
    }

}
