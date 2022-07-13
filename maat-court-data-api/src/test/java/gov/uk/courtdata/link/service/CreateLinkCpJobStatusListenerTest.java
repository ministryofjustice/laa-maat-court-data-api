package gov.uk.courtdata.link.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.CpJobStatus;
import gov.uk.courtdata.service.QueueMessageLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateLinkCpJobStatusListenerTest {

    @Mock
    private Gson gson;
    @Mock
    private CreateLinkCpJobStatusService createLinkCpJobStatusService;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @InjectMocks
    private CreateLinkCpJobStatusListener createLinkCpJobStatusListener;


    @Test
    public void givenQueueMessageIsReceived_whenQueueListenerIsInvoked_thenCallCreateLinkCpJobStatusService() {

        CpJobStatus cpJobStatus = CpJobStatus.builder().build();
        String message = "some queue json type message";

        when(gson.fromJson(message, CpJobStatus.class)).thenReturn(cpJobStatus);
        createLinkCpJobStatusListener.receive(message);

        verify(createLinkCpJobStatusService).execute(cpJobStatus);
        verify(queueMessageLogService).createLog(MessageType.CREATE_LINK_CP_STATUS_JOB, message);
    }
}
