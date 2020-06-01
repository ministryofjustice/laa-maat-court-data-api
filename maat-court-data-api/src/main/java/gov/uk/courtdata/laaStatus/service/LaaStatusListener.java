package gov.uk.courtdata.laaStatus.service;

import com.google.gson.Gson;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laaStatus.builder.CourtDataDTOBuilder;
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

    private final LaaStatusService laaStatusService;

    private final LaaStatusPostCDAService laaStatusPostCDAService;

    private final Gson gson;

    private CourtDataDTOBuilder courtDataDTOBuilder;

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


        CourtDataDTO courtDataDTO = courtDataDTOBuilder.build(laaStatusUpdate);
        log.info("POST Rep Order update to CDA {}", logging);
        laaStatusPostCDAService.process(courtDataDTO);
        log.info("Update LAA status {}", logging);
        laaStatusService.execute(courtDataDTO);
        log.info("After laa update {}", logging);
    }


}
