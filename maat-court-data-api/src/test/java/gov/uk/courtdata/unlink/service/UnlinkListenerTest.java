package gov.uk.courtdata.unlink.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.service.QueueMessageLogService;
import gov.uk.courtdata.unlink.processor.UnLinkProcessor;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UnlinkListenerTest {

    @Mock
    private Gson gson;

    @InjectMocks
    private UnlinkListener unlinkListener;

    @Mock
    private UnLinkProcessor unLinkProcessor;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenSqsIsReceived_whenUnlinkIsInvoked_thenUnlinkCase() {

        String message = "this is a SQS payload in JSON format";
        Unlink unlink = Unlink.builder()
                .maatId(1111111)
                .build();
        when(gson.fromJson(message, Unlink.class)).thenReturn(unlink);
        unlinkListener.receive(message);

        verify(unLinkProcessor).process(any());
        verify(queueMessageLogService).createLog(MessageType.UNLINK, message);

    }
}