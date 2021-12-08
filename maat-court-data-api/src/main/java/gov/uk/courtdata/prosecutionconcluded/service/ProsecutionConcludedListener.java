package gov.uk.courtdata.prosecutionconcluded.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.hearing.crowncourt.service.CrownCourtHearingService;
import gov.uk.courtdata.model.crowncourt.ProsecutionConcluded;
import gov.uk.courtdata.service.QueueMessageLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProsecutionConcludedListener {

    private final Gson gson;

    private final ProsecutionConcludedService prosecutionConcludedService;

    private final QueueMessageLogService queueMessageLogService;

    private final CrownCourtHearingService crownCourtHearingService;

    @JmsListener(destination = "${cloud-platform.aws.sqs.queue.prosecutionConcluded}")
    public void receive(@Payload final String message) {
        MDC.put(LoggingData.REQUEST_TYPE.getValue(), MessageType.PROSECUTION_CONCLUDED.name());
        queueMessageLogService.createLog(MessageType.PROSECUTION_CONCLUDED, message);
        ProsecutionConcluded prosecutionConcluded = gson.fromJson(message, ProsecutionConcluded.class);


        prosecutionConcludedService.execute(prosecutionConcluded);
    }
}
