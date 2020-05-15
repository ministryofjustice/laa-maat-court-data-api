package gov.uk.courtdata.laaStatus.service;

import com.google.gson.Gson;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.LaaTransactionLogging;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * <code>LaaStatusListener</code> a JMS listener to consume laa status message and trigger
 * status update in maat db.
 */
@Slf4j
@AllArgsConstructor
@Service
public class LaaStatusListener {

    private final LaaStatusService laaStatusUpdateService;

    private final LaaStatusPostCDAService laaStatusPostCDAService;

    private final Gson gson;

    /**
     * @param message
     * @throws JmsException
     */
    @JmsListener(destination = "${cloud-platform.aws.sqs.queue.laaStatus}")
    public void receive(@Payload final String message) throws JmsException {

        CaseDetails laaStatusUpdate = gson.fromJson(message, CaseDetails.class);
        String logging = LaaTransactionLogging.builder()
                .maatId(laaStatusUpdate.getMaatId())
                .laaTransactionId(laaStatusUpdate.getLaaTransactionId()).build().toString();
        log.info("POST to CDA {}" , logging);
        laaStatusPostCDAService.process(laaStatusUpdate);
        log.info("After laa update {}", logging);
        laaStatusUpdateService.execute(laaStatusUpdate);
    }


}
