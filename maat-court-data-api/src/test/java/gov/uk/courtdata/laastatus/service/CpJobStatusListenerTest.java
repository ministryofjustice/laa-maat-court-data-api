package gov.uk.courtdata.laastatus.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.QueueMessageType;
import gov.uk.courtdata.model.CpJobStatus;
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
public class CpJobStatusListenerTest {

    @InjectMocks
    private LaaStatusJobListener laaStatusJobListener;
    @Mock
    private Gson gson;
    @Mock
    private LaaStatusJobService laaStatusJobService;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenJSONMessageIsReceived_whenLaaStatusJobListenerIsInvoked_thenLaaStatusJobServiceIsCalled() {
        //given
        CpJobStatus cpJobStatus = CpJobStatus.builder().build();
        String message = "Test JSON";
        //when
        when(gson.fromJson(message, CpJobStatus.class)).thenReturn(cpJobStatus);
        laaStatusJobListener.receive(message);
        //then
        verify(laaStatusJobService, times(1)).execute(cpJobStatus);
        verify(queueMessageLogService, times(1)).createLog(QueueMessageType.LAA_STATUS_JOB, message);
    }

}
