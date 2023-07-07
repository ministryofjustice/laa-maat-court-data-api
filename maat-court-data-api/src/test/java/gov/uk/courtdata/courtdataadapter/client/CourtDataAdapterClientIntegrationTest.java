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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtDataAdapterClientIntegrationTest {

    private static final Integer MAX_RETRIES = 3;
    private static final Integer MIN_BACKOFF_PERIOD = 1;
    private static final Double JITTER = 0.75;
    private static final String LOCALHOST = "http://localhost:%s";
    private static final String LAA_STATUS_URL = "cda-test/laaStatus";
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
        baseUrl = String.format(LOCALHOST, mockCourtDataAdapterApi.getPort());
        webClient = buildWebClient();
    }

    private WebClient buildWebClient() {
        CourtDataAdapterOAuth2ClientConfig courtDataAdapterOAuth2ClientConfig = new CourtDataAdapterOAuth2ClientConfig();
        return courtDataAdapterOAuth2ClientConfig.webClient(baseUrl, authClientManager, MAX_RETRIES, MIN_BACKOFF_PERIOD, JITTER);
    }

    @Test
    public void givenAValidLaaStatusObject_whenPostLaaStatusIsInvoked_andTwoInternalServerErrorsOccurInCDA_thenTheRequestIsSentCorrectlyAndAnAcceptedResponseIsReceived() {
        // given
        courtDataAdapterClient = new CourtDataAdapterClient(webClient, gsonBuilder, queueMessageLogService, courtDataAdapterClientConfig);
        when(courtDataAdapterClientConfig.getLaaStatusUrl()).thenReturn(LAA_STATUS_URL);
        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.ACCEPTED.value()));
        Map<String, String> headers = Map.of("test-header", "test-header-value");
        LaaStatusUpdate testStatusObject = getTestLaaStatusObject();

        // when
        courtDataAdapterClient.postLaaStatus(testStatusObject, headers);

        //then
        String jsonBody = gsonBuilder.create().toJson(testStatusObject);
        verify(queueMessageLogService, atLeastOnce())
                .createLog(MessageType.LAA_STATUS_UPDATE, jsonBody);
        // TODO: verify that retry messages are in the logs
        // TODO: verify that accepted message is in the logs
    }

    // TODO: test for exhausted retries

    private LaaStatusUpdate getTestLaaStatusObject() {
        return LaaStatusUpdate.builder().data(RepOrderData.builder().type("test-representation_order").build()).build();
    }

}
