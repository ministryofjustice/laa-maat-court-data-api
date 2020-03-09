package gov.uk.courtdata.laaStatus.service;


import com.google.gson.Gson;
import gov.uk.courtdata.model.CaseDetails;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
        CaseDetails caseDetails = CaseDetails.builder().build();

        //when
        when(gson.toJson(caseDetails)).thenReturn("Case Details");
        laaStatusPublisher.publish(caseDetails);

        //then
        verify(defaultJmsTemplate, times(1)).convertAndSend("${aws.sqs.queue.laaStatusUpdate}", "Case Details");
    }
}
