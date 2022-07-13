package gov.uk.courtdata.laastatus.service;


import com.google.gson.Gson;
import gov.uk.courtdata.model.CaseDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LaaStatusPublisherTest {

    @InjectMocks
    private LaaStatusPublisher laaStatusPublisher;
    @Mock
    private JmsTemplate defaultJmsTemplate;
    @Mock
    private Gson gson;


    @Test
    public void givenACaseDetail_whenPublisherIsInvoked_thenMessageIsPublished() {

        //given
        CaseDetails caseDetails = CaseDetails.builder().laaTransactionId(UUID.randomUUID()).build();

        //when
        String sqsQueue = null;
        when(gson.toJson(caseDetails)).thenReturn("Case Details");
        laaStatusPublisher.publish(caseDetails);

        //then
        verify(defaultJmsTemplate, times(1)).convertAndSend(sqsQueue, "Case Details");
    }
}
