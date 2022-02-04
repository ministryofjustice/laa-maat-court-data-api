package gov.uk.courtdata.hearing.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.service.QueueMessageLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class HearingResultedListener {

    private final Gson gson;

    private final HearingResultedService hearingResultedService;

    private final QueueMessageLogService queueMessageLogService;

    @JmsListener(destination = "${cloud-platform.aws.sqs.queue.hearingResulted}")
    public void receive(@Payload final String message) {

        MDC.put(LoggingData.REQUEST_TYPE.getValue(), MessageType.HEARING.name());
        queueMessageLogService.createLog(MessageType.HEARING,message);
        HearingResulted hearingResulted = gson.fromJson(message, HearingResulted.class);

        if (hearingResulted.getHearingId() == null) hearingResulted.setHearingId(UUID.randomUUID());

        hearingResultedService.execute(hearingResulted);
    }
}
