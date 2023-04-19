package gov.uk.courtdata.eforms.service;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import com.fasterxml.jackson.core.JsonProcessingException;
import gov.uk.courtdata.eformsApplication.builder.EformsStagingBuilder;
import gov.uk.courtdata.eformsApplication.dto.EformsStagingDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.eformsApplication.EformsApplication;
import gov.uk.courtdata.service.QueueMessageLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
@Service
@XRayEnabled
public class EformsStagingListener {
    private final EformsStagingBuilder eformsStagingBuilder;
    private final QueueMessageLogService queueMessageLogService;
    private final EformsStagingService eformsStagingService;
    //todo: get a sqs name and configure in the Cloud Formation and app yaml etc.
    @JmsListener(destination = "${cloud-platform.aws.sqs.queue.hearingResulted}", concurrency = "1")
    public void receive(@Payload final String message) throws SQLException, JsonProcessingException {
        MDC.put(LoggingData.REQUEST_TYPE.getValue(), MessageType.CRIME_APPLY_EFORMS.name());

        //todo: maybe not need otherwise - when maat-id not available it will set to -1
        queueMessageLogService.createLog(MessageType.CRIME_APPLY_EFORMS, message);
        //gson model - tp
        //todo: maybe call a mapper here before sending it to the
        EformsApplication eformsApplication = EformsApplication.builder().build();
        EformsStagingDTO eformsStagingDTO= eformsStagingBuilder.build(eformsApplication);

        eformsStagingService.execute(eformsStagingDTO);
    }
}