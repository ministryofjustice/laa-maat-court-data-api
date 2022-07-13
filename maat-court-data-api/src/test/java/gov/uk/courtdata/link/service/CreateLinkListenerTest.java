package gov.uk.courtdata.link.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.service.QueueMessageLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateLinkListenerTest {

    @InjectMocks
    private CreateLinkListener createLinkListener;
    @Mock
    private Gson gson;
    @Mock
    private CreateLinkService createLinkService;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @Test
    public void givenJSONMessageIsReceived_whenCreateLinkListenerIsInvoked_thenCreateLinkServiceIsCalled() {
        //given
        CaseDetails caseDetails = CaseDetails.builder().build();
        String message = "Test JSON";
        //when
        when(gson.fromJson(message, CaseDetails.class)).thenReturn(caseDetails);
        createLinkListener.receive(message);
        //then
        verify(createLinkService, times(1)).saveAndLink(caseDetails);
        verify(queueMessageLogService, times(1)).createLog(MessageType.LINK, message);
    }
}