package gov.uk.courtdata.unlink.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.service.QueueMessageLogService;
import gov.uk.courtdata.unlink.processor.UnLinkProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UnlinkListenerTest {

    @Mock
    private Gson gson;

    @InjectMocks
    private UnlinkListener unlinkListener;

    @Mock
    private UnLinkProcessor unLinkProcessor;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @Test
    public void givenSqsIsReceived_whenUnlinkIsInvoked_thenUnlinkCase() {

        String message = "this is a SQS payload in JSON format";
        Unlink unlink = Unlink.builder()
                .maatId(1111111)
                .build();
        when(gson.fromJson(message, Unlink.class)).thenReturn(unlink);
        unlinkListener.receive(message, new MessageHeaders(new HashMap<>()));

        verify(unLinkProcessor).process(any());
        verify(queueMessageLogService).createLog(MessageType.UNLINK, message);

    }
}