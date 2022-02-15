package gov.uk.courtdata.prosecutionconcluded;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.prosecutionconcluded.service.ProsecutionConcludedListener;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.service.ProsecutionConcludedService;
import gov.uk.courtdata.service.QueueMessageLogService;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

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


    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenJSONMessageIsReceived_whenProsecutionConcludedListenerIsInvoked_thenProsecutionConcludedServiceIsCalled() {
        //given
        ProsecutionConcluded prosecutionConcludedRequest = ProsecutionConcluded.builder().build();
        String message = "Test JSON";
        //when
        when(gson.fromJson(message, ProsecutionConcluded.class)).thenReturn(prosecutionConcludedRequest);
        prosecutionConcludedListener.receive(message);
        //then
        verify(prosecutionConcludedService).execute(prosecutionConcludedRequest);
        verify(queueMessageLogService).createLog(MessageType.PROSECUTION_CONCLUDED, message);
    }
}