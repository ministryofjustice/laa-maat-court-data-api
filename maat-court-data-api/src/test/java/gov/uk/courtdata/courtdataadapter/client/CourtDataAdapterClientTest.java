package gov.uk.courtdata.courtdataadapter.client;

import com.google.gson.GsonBuilder;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.laastatus.LaaStatusUpdate;
import gov.uk.courtdata.model.laastatus.RepOrderData;
import gov.uk.courtdata.service.QueueMessageLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.commons.client.RestAPIClient;
import uk.gov.justice.laa.crime.commons.exception.APIClientException;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourtDataAdapterClientTest {

    private final String hearingUrl = "cda-test/hearing/{hearingId}";

    @Mock
    private RestAPIClient cdaAPIClient;

    @Mock
    private CourtDataAdapterClientConfig courtDataAdapterClientConfig;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @Mock
    private GsonBuilder gsonBuilder;

    @InjectMocks
    private CourtDataAdapterClient courtDataAdapterClient;

    @Test
    void givenAValidHearingId_whenTriggerHearingProcessingIsInvoked_thenTheRequestIsSentCorrectly() {
        UUID testHearingId = UUID.randomUUID();
        String testTransactionId = UUID.randomUUID().toString();
        when(courtDataAdapterClientConfig.getHearingUrl()).thenReturn(hearingUrl);
        courtDataAdapterClient.triggerHearingProcessing(testHearingId, testTransactionId);
        verify(cdaAPIClient).get(
                any(),
                anyString(),
                anyMap(),
                ArgumentMatchers.any(),
                any(UUID.class)
        );
    }

    @Test
    void givenAValidHearingId_whenTriggerHearingProcessingIsInvokedAndTheCallFails_thenFailureIsHandled() {
        UUID testHearingId = UUID.randomUUID();
        String testTransactionId = UUID.randomUUID().toString();
        when(courtDataAdapterClientConfig.getHearingUrl()).thenReturn(hearingUrl);
        when(cdaAPIClient.get(
                any(),
                anyString(),
                anyMap(),
                ArgumentMatchers.any(),
                any(UUID.class)
        )).thenThrow(new APIClientException());

        assertThatThrownBy(() -> courtDataAdapterClient.triggerHearingProcessing(testHearingId, testTransactionId))
                .isInstanceOf(APIClientException.class);
    }

    @Test
    public void givenAValidLaaStatusObject_whenPostLaaStatusIsInvoked_thenTheRequestIsSentCorrectly() {
        when(gsonBuilder.create()).thenReturn(new GsonBuilder().create());
        Map<String, String> headers = Map.of("test-header", "test-header-value");
        LaaStatusUpdate testStatusObject = getTestLaaStatusObject();
        courtDataAdapterClient.postLaaStatus(testStatusObject, headers);
        String jsonBody = gsonBuilder.create().toJson(testStatusObject);
        verify(queueMessageLogService, atLeastOnce())
                .createLog(MessageType.LAA_STATUS_UPDATE, jsonBody);
        verify(cdaAPIClient).post(any(), any(), any(), any());
    }

    @Test
    void givenAInValidRequest_whenPostLaaStatusIsInvoked_thenFailureIsHandled() {
        when(gsonBuilder.create()).thenReturn(new GsonBuilder().create());
        when(cdaAPIClient.post(
                any(),
                any(),
                any(),
                any()
        )).thenThrow(new APIClientException());
        Map<String, String> headers = Map.of("test-header", "test-header-value");
        LaaStatusUpdate testStatusObject = getTestLaaStatusObject();
        assertThatThrownBy(() -> courtDataAdapterClient.postLaaStatus(testStatusObject, headers))
                .isInstanceOf(APIClientException.class);
    }

    private LaaStatusUpdate getTestLaaStatusObject() {
        return LaaStatusUpdate.builder().data(RepOrderData.builder().type("test-representation_order").build()).build();
    }
}
