package gov.uk.courtdata.assessment.service;

import com.google.gson.Gson;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.assessment.PostProcessing;
import gov.uk.courtdata.service.QueueMessageLogService;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeansAssessmentPostProcessingListenerTest {

    @InjectMocks
    private MeansAssessmentPostProcessingListener meansAssessmentPostProcessingListener;
    @Mock
    private Gson gson;
    @Mock
    private PostProcessingService postProcessingService;
    @Mock
    private QueueMessageLogService queueMessageLogService;
    @Mock
    private MaatIdValidator maatIdValidator;


    @Test
    public void givenJSONMessageIsReceived_whenPostProcessingListenerIsInvoked_thenPostProcessingServiceIsCalled() {
        //given
        PostProcessing postProcessing = TestModelDataBuilder.getPostProcessing();
        String message = "{\"repId\":5232177, \"laaTransactionId\":\"CASNUM-ABC123\"}";

        //when
        when(gson.fromJson(message, PostProcessing.class)).thenReturn(postProcessing);
        meansAssessmentPostProcessingListener.receive(message);
        //then
        verify(postProcessingService, times(1)).execute(postProcessing);
        verify(queueMessageLogService, times(1)).createLog(MessageType.MEANS_ASSESSMENT_POST_PROCESSING, message);

    }
}
