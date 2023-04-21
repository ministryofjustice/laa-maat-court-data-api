package gov.uk.courtdata.eform.service;

import gov.uk.courtdata.service.QueueMessageLogService;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EformsStagingListenerTest {

    @InjectMocks
    private EformsStagingListener eformsStagingListener;

    @Mock
    private EformsStagingService eformsStagingService;

    @Mock
    private QueueMessageLogService queueMessageLogService;

    @Test @Disabled
    public void givenSqsIsReceived_whenEformsStagingListenerListenerIsInvoked_thenProcessRequest() {
        //given

        // TODO Complete this
        String message = "sqs payload";

        //when
        //eformsStagingListener.receive(message);
        //then
        //verify(eformsStagingService).execute(any());
        //verify(queueMessageLogService).createLog(MessageType.CRIME_APPLY_EFORMS, message);
    }

}