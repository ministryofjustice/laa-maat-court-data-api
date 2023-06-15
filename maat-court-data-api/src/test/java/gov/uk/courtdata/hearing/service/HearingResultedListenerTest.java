package gov.uk.courtdata.hearing.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.service.QueueMessageLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class HearingResultedListenerTest {

    @InjectMocks
    private HearingResultedListener hearingResultedListener;
    @Mock
    private Gson gson;
    @Mock
    private HearingResultedService hearingResultedService;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @Test
    public void givenJSONMessageIsReceived_whenHearingResultedListenerIsInvoked_thenHearingResultedServiceIsCalled() {
        //given
        HearingResulted laaHearingDetails = HearingResulted.builder().build();
        String message = "{\"laaTransactionId\":\"c77c96ff-7cad-44cc-9e12-5bc80f5f2d9e\" ,\n" +
                "  \"caseUrn\":\"CASNUM-ABC123\",\n" +
                "  \"maatId\": \"null\"}";
        //when
        when(gson.fromJson(message, HearingResulted.class)).thenReturn(laaHearingDetails);
        hearingResultedListener.receive(message, new MessageHeaders(new HashMap<>()));
        //then
        verify(hearingResultedService, times(1)).execute(laaHearingDetails);
        verify(queueMessageLogService, times(1)).createLog(MessageType.HEARING, message);
        assertNotNull(laaHearingDetails.getHearingId());
    }
}
