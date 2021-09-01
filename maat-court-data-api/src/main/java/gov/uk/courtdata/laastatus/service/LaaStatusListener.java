package gov.uk.courtdata.laastatus.service;

import com.google.gson.Gson;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.laastatus.builder.CourtDataDTOBuilder;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.service.QueueMessageLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * <code>LaaStatusListener</code> a JMS listener to consume laa status message and trigger
 * status update in maat db.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class LaaStatusListener {

    private final LaaStatusService laaStatusService;

    private final LaaStatusPostCDAService laaStatusPostCDAService;

    private final Gson gson;

    private final CourtDataDTOBuilder courtDataDTOBuilder;

    private final QueueMessageLogService queueMessageLogService;


    @Value("${feature.postMvpEnabled}")
    private String isPostMVPEnabled;

    /**
     * @param message queue message body
     * @throws JmsException jms exception
     */
    @JmsListener(destination = "${cloud-platform.aws.sqs.queue.laaStatus}")
    public void receive(@Payload final String message) {

        queueMessageLogService.createLog(MessageType.LAA_STATUS, message);
        CaseDetails laaStatusUpdate = gson.fromJson(message, CaseDetails.class);

        CourtDataDTO courtDataDTO = courtDataDTOBuilder.build(laaStatusUpdate);
        if (Boolean.TRUE.toString().equalsIgnoreCase(isPostMVPEnabled)) {
            processLaaStatus(courtDataDTO);
        } else {
            processLaaStatusServiceForCDA(courtDataDTO);
            processLaaStatusServiceForMLA(courtDataDTO);
        }
    }

    private void processLaaStatus(CourtDataDTO courtDataDTO) {
        processLaaStatusServiceForMLA(courtDataDTO);
        processLaaStatusServiceForCDA(courtDataDTO);
    }

    private void processLaaStatusServiceForCDA(CourtDataDTO courtDataDTO) {
        log.info("Start - POST Rep Order update to CDA");
        laaStatusPostCDAService.process(courtDataDTO);
        log.info("Ends - POST Rep Order update to CDA");
    }

    private void processLaaStatusServiceForMLA(CourtDataDTO courtDataDTO) {

        if (!courtDataDTO.getCaseDetails().isOnlyForCDAService()) {
            log.info("Start - Update LAA status to MLA");
            laaStatusService.execute(courtDataDTO);
            log.info("Ends - After laa update to MLA");
        }
    }
}
