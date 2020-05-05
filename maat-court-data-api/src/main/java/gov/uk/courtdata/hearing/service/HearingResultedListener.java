package gov.uk.courtdata.hearing.service;

import com.google.gson.Gson;
import gov.uk.courtdata.model.hearing.HearingDetails;
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
    private final HearingResultedService hearingResultedService;

    @JmsListener(destination = "${cloud-platform.aws.sqs.queue.hearingResulted}")
    public void receive(@Payload final String message) throws JmsException {

        HearingDetails laaHearingDetails = gson.fromJson(message, HearingDetails.class);
        hearingResultedService.process(laaHearingDetails);
        log.info("Crown Court Hearing Processing has been completed for MAAT ID and Tx:  {0}, {1}",
                laaHearingDetails.getMaatId() , laaHearingDetails.getLaaTransactionId());
    }
}




