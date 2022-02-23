package gov.uk.courtdata.prosecutionconcluded;

import com.google.gson.Gson;
import gov.uk.courtdata.config.AmazonSQSConfig;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.enums.PleaTrialOutcome;
import gov.uk.courtdata.prosecutionconcluded.service.ProsecutionConcludedListener;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.service.ProsecutionConcludedService;
import gov.uk.courtdata.service.QueueMessageLogService;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProsecutionConcludedListenerTest {

    @InjectMocks
    private ProsecutionConcludedListener prosecutionConcludedListener;
    @Mock
    private Gson gson;
    @Mock
    private ProsecutionConcludedService prosecutionConcludedService;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @Mock
    private AmazonSQSConfig amazonSQSConfig;


    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test @Ignore
    public void givenJSONMessageIsReceived_whenProsecutionConcludedListenerIsInvoked_thenProsecutionConcludedServiceIsCalled() {
        //given

        String message = getSqsMessagePayload();
        Gson locaGson = new Gson();
        ProsecutionConcluded prosecutionConcluded = locaGson.fromJson(getSqsMessagePayload(), ProsecutionConcluded.class);
        String originatingHearingId="61600a90-89e2-4717-aa9b-a01fc66130c1";


        //when
        when(gson.fromJson(message, ProsecutionConcluded.class)).thenReturn(prosecutionConcluded);
        Objects.requireNonNull(when(amazonSQSConfig.awsSqsClient()));
        prosecutionConcludedListener.receive();
        //then
        verify(prosecutionConcludedService).execute(prosecutionConcluded);
        verify(queueMessageLogService).createLog(MessageType.PROSECUTION_CONCLUDED, message);

        assertEquals("998984a0-ae53-466c-9c13-e0c84c1fd581", prosecutionConcluded.getProsecutionCaseId().toString());
        assertEquals(true, prosecutionConcluded.isConcluded());
        assertEquals("aa07e234-7e80-4be1-a076-5ab8a8f49df5", prosecutionConcluded.getDefendantId().toString());
        assertEquals(originatingHearingId, prosecutionConcluded.getHearingIdWhereChangeOccurred().toString());
        assertEquals(1, prosecutionConcluded.getOffenceSummary().size());

        assertEquals("ed0e9d59-cc1c-4869-8fcd-464caf770744", prosecutionConcluded.getOffenceSummary().get(0).getOffenceId().toString());
        assertEquals("PT00011", prosecutionConcluded.getOffenceSummary().get(0).getOffenceCode());
        assertEquals(true, prosecutionConcluded.getOffenceSummary().get(0).isProceedingsConcluded());
        assertEquals("2022-02-01", prosecutionConcluded.getOffenceSummary().get(0).getProceedingsConcludedChangedDate());

        assertEquals(PleaTrialOutcome.GUILTY.name(), prosecutionConcluded.getOffenceSummary().get(0).getPlea().getValue());
        assertEquals(originatingHearingId, prosecutionConcluded.getOffenceSummary().get(0).getPlea().getOriginatingHearingId().toString());
        assertEquals("2022-02-01", prosecutionConcluded.getOffenceSummary().get(0).getPlea().getPleaDate());

        assertEquals(PleaTrialOutcome.GUILTY.name(), prosecutionConcluded.getOffenceSummary().get(0).getVerdict().getVerdictType().getCategoryType());

        assertEquals(PleaTrialOutcome.GUILTY.name(), prosecutionConcluded.getOffenceSummary().get(0).getVerdict().getVerdictType().getCategory());
        assertEquals(4126, prosecutionConcluded.getOffenceSummary().get(0).getVerdict().getVerdictType().getSequence());

        assertEquals(originatingHearingId, prosecutionConcluded.getMetadata().getLaaTransactionId().toString());
    }

    private String getSqsMessagePayload() {
        return "{\n" +
                "    \"prosecutionCaseId\": \"998984a0-ae53-466c-9c13-e0c84c1fd581\",\n" +
                "    \"defendantId\": \"aa07e234-7e80-4be1-a076-5ab8a8f49df5\",\n" +
                "    \"isConcluded\": true,\n" +
                "    \"hearingIdWhereChangeOccurred\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "    \"offenceSummary\": [\n" +
                "        {\n" +
                "            \"offenceId\": \"ed0e9d59-cc1c-4869-8fcd-464caf770744\",\n" +
                "            \"offenceCode\": \"PT00011\",\n" +
                "            \"proceedingsConcluded\": true,\n" +
                "            \"proceedingsConcludedChangedDate\": \"2022-02-01\",\n" +
                "            \"plea\": {\n" +
                "                \"originatingHearingId\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "                \"value\": \"GUILTY\",\n" +
                "                \"pleaDate\": \"2022-02-01\"\n" +
                "            },\n" +
                "            \"verdict\": {\n" +
                "                \"verdictDate\": \"2022-02-01\",\n" +
                "                \"originatingHearingId\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\",\n" +
                "                \"verdictType\": {\n" +
                "                    \"description\": \"GUILTY\",\n" +
                "                    \"category\": \"GUILTY\",\n" +
                "                    \"categoryType\": \"GUILTY\",\n" +
                "                    \"sequence\": 4126,\n" +
                "                    \"verdictTypeId\": null\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    ],\n" +
                "    \"maatId\": \"6039349\",\n" +
                "    \"metadata\": {\n" +
                "        \"laaTransactionId\": \"61600a90-89e2-4717-aa9b-a01fc66130c1\"\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "\n";
    }
}