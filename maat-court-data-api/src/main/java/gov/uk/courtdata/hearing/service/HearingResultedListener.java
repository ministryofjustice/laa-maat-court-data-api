package gov.uk.courtdata.hearing.service;

import com.google.gson.Gson;
import gov.uk.courtdata.hearing.impl.HearingResultedImpl;
import gov.uk.courtdata.hearing.model.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HearingResultedListener {

    private final Gson gson;
    private final HearingResultedImpl hearingResultedImpl;

    @JmsListener(destination = "${cloud-platform.aws.sqs.queue.hearingResulted}")
    public void receive(@Payload final String message) throws JmsException {

        log.info("Received JSON Message for Hearing Resulted {}", message);
        HearingResulted laaHearingResulted = gson.fromJson(message, HearingResulted.class);
        hearingResultedImpl.execute(laaHearingResulted);
    }
}


