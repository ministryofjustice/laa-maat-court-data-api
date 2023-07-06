package gov.uk.courtdata.courtdataadapter.client;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.model.laastatus.RepOrderData;
import gov.uk.courtdata.service.QueueMessageLogService;
import okhttp3.mockwebserver.MockResponse;
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
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtDataAdapterClientIntegrationTest {

    public static MockWebServer mockCourtDataAdapterApi;

    private String baseUrl;

    @Mock
    private CourtDataAdapterClientConfig courtDataAdapterClientConfig;
    @Mock
    private QueueMessageLogService queueMessageLogService;

    private GsonBuilder gsonBuilder;
    private CourtDataAdapterClient courtDataAdapterClient;
    private WebClient webClient;

    @Mock
    private OAuth2AuthorizedClientManager authClientManager;

    @BeforeEach
    public void setup() throws IOException {
        gsonBuilder = new GsonBuilder();
        mockCourtDataAdapterApi = new MockWebServer();
        mockCourtDataAdapterApi.start();
        baseUrl = String.format("http://localhost:%s", mockCourtDataAdapterApi.getPort());

        webClient = buildWebClient();
        System.out.println("Test");
    }

    private WebClient buildWebClient() {
        CourtDataAdapterOAuth2ClientConfig courtDataAdapterOAuth2ClientConfig = new CourtDataAdapterOAuth2ClientConfig();
        return courtDataAdapterOAuth2ClientConfig.webClient(baseUrl, authClientManager, 3, 5, 0.75);
    }

    @Test
    public void testBlah() {
        courtDataAdapterClient = new CourtDataAdapterClient(webClient, gsonBuilder, queueMessageLogService, courtDataAdapterClientConfig);

        String laaStatusUrl = "cda-test/laaStatus";
        when(courtDataAdapterClientConfig.getLaaStatusUrl()).thenReturn(laaStatusUrl);

        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.ACCEPTED.value()));

        Map<String, String> headers = Map.of("test-header", "test-header-value");
        LaaStatusUpdate testStatusObject = getTestLaaStatusObject();

        courtDataAdapterClient.postLaaStatus(testStatusObject, headers);

        String jsonBody = gsonBuilder.create().toJson(testStatusObject);

        verify(queueMessageLogService, atLeastOnce())
                .createLog(MessageType.LAA_STATUS_UPDATE, jsonBody);

    }

    private LaaStatusUpdate getTestLaaStatusObject() {
        return LaaStatusUpdate.builder().data(RepOrderData.builder().type("test-representation_order").build()).build();
    }

}
