package gov.uk.courtdata.hearing.service;

import com.google.gson.Gson;
import gov.uk.courtdata.model.hearing.HearingDetails;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HearingResultedListenerTest {

    @InjectMocks
    private HearingResultedListener hearingResultedListener;
    @Mock
    private Gson gson;
    @Mock
    private HearingResultedService hearingResultedService;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenJSONMessageIsReceived_whenHearingResultedListenerIsInvoked_thenHearingResultedServiceIsCalled() {
        //given
        HearingDetails laaHearingDetails = HearingDetails.builder().build();
        String message = "Test JSON";
        //when
        when(gson.fromJson(message, HearingDetails.class)).thenReturn(laaHearingDetails);
        hearingResultedListener.receive(message);
        //then
        verify(hearingResultedService, times(1)).process(laaHearingDetails);
    }
}
