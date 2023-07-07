package gov.uk.courtdata.courtdataadapter.client;

import ch.qos.logback.classic.Level;
import com.google.gson.GsonBuilder;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.exception.ApiClientException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

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

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    public void setup() throws IOException {
        gsonBuilder = new GsonBuilder();
        mockCourtDataAdapterApi = new MockWebServer();
        mockCourtDataAdapterApi.start();
        baseUrl = String.format(LOCALHOST, mockCourtDataAdapterApi.getPort());
        webClient = buildWebClient();

        Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    private WebClient buildWebClient() {
        CourtDataAdapterOAuth2ClientConfig courtDataAdapterOAuth2ClientConfig = new CourtDataAdapterOAuth2ClientConfig();
        return courtDataAdapterOAuth2ClientConfig.webClient(baseUrl, authClientManager, MAX_RETRIES, MIN_BACKOFF_PERIOD, JITTER);
    }

    @Test
    public void givenAValidLaaStatusObject_whenPostLaaStatusIsInvoked_andSomeErrorResponsesReceivedFromCDAAreUnderMaxRetries_thenTheRequestIsSentCorrectlyAndAnAcceptedResponseIsReceived() {
        // given
        courtDataAdapterClient = new CourtDataAdapterClient(webClient, gsonBuilder, queueMessageLogService, courtDataAdapterClientConfig);
        when(courtDataAdapterClientConfig.getLaaStatusUrl()).thenReturn(LAA_STATUS_URL);
        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
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

        assertTrue(listAppender.list.stream()
                .anyMatch(event -> event.getLevel() == Level.WARN && event.getFormattedMessage().contains("Call to service failed, retrying: 3/3")));
        assertTrue(listAppender.list.stream()
                .anyMatch(event -> event.getLevel() == Level.INFO && event.getFormattedMessage().contains("LAA status update posted Optional[202 ACCEPTED]")));
    }

    @Test
    public void givenAValidLaaStatusObject_whenPostLaaStatusIsInvoked_andErrorResponsesReceivedFromCDAExceedMaxRetries_thenAnErrorIsThrown() {
        // given
        courtDataAdapterClient = new CourtDataAdapterClient(webClient, gsonBuilder, queueMessageLogService, courtDataAdapterClientConfig);
        when(courtDataAdapterClientConfig.getLaaStatusUrl()).thenReturn(LAA_STATUS_URL);
        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        mockCourtDataAdapterApi.enqueue(new MockResponse().setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        Map<String, String> headers = Map.of("test-header", "test-header-value");
        LaaStatusUpdate testStatusObject = getTestLaaStatusObject();

        // when
        ApiClientException error = assertThrows(ApiClientException.class, () -> courtDataAdapterClient.postLaaStatus(testStatusObject, headers));

        //then
        String jsonBody = gsonBuilder.create().toJson(testStatusObject);
        verify(queueMessageLogService, atLeastOnce())
                .createLog(MessageType.LAA_STATUS_UPDATE, jsonBody);
        assertTrue(listAppender.list.stream()
                .anyMatch(event -> event.getLevel() == Level.WARN && event.getFormattedMessage().contains("Call to service failed, retrying: 3/3")));
        assertTrue(error.getMessage().contains("Call to service failed. Retries exhausted: 3/3."));
    }

    private LaaStatusUpdate getTestLaaStatusObject() {
        return LaaStatusUpdate.builder().data(RepOrderData.builder().type("test-representation_order").build()).build();
    }

}
