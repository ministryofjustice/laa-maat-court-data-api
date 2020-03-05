package gov.uk.courtdata.laaStatus.service;

import com.google.gson.Gson;
import gov.uk.courtdata.model.CaseDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LaaStatusPublisher {

    private final JmsTemplate defaultJmsTemplate;
    private final Gson gson;


    public void publish(CaseDetails caseDetails) {

        String laaStatusUpdateJSON = gson.toJson(caseDetails);
        defaultJmsTemplate.convertAndSend("${aws.sqs.queue.laaStatusUpdate}", laaStatusUpdateJSON);
        log.debug("A JSON Message {} has been published to the Queue {}", "${aws.sqs.queue.laaStatusUpdate}", laaStatusUpdateJSON);

    }
}