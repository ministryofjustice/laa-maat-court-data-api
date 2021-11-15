package gov.uk.courtdata.hearing.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.JurisdictionType;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.service.QueueMessageLogService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class HearingResultedListenerTest {

    @InjectMocks
    private HearingResultedListener hearingResultedListener;
    @Mock
    private Gson gson;
    @Mock
    private HearingResultedService hearingResultedService;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenJSONMessageIsReceived_whenHearingResultedListenerIsInvoked_thenHearingResultedServiceIsCalled() {
        //given
        HearingResulted laaHearingDetails = HearingResulted.builder().build();
        String message = "{\"laaTransactionId\":\"c77c96ff-7cad-44cc-9e12-5bc80f5f2d9e\" ,\n" +
                "  \"caseUrn\":\"CASNUM-ABC123\",\n" +
                "  \"maatId\": \"null\"}";
        //when
        when(gson.fromJson(message, HearingResulted.class)).thenReturn(laaHearingDetails);
        hearingResultedListener.receive(message);
        //then
        verify(hearingResultedService, times(1)).execute(laaHearingDetails);
        verify(queueMessageLogService, times(1)).createLog(MessageType.HEARING, message);
        assertNotNull(laaHearingDetails.getHearingId());
    }
}
