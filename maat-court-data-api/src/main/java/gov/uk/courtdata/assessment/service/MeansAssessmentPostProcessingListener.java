package gov.uk.courtdata.assessment.service;

import com.google.gson.Gson;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.enums.MessageType;
import gov.uk.courtdata.model.assessment.PostProcessing;
import gov.uk.courtdata.service.QueueMessageLogService;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class MeansAssessmentPostProcessingListener {

    private final Gson gson;

    private final MaatIdValidator maatIdValidator;
    private final PostProcessingService postProcessingService;
    private final QueueMessageLogService queueMessageLogService;

    @JmsListener(destination = "${cloud-platform.aws.sqs.queue.meansAssessmentPostProcessing}", concurrency = "1")
    public void receive(@Payload final String message) {
        queueMessageLogService.createLog(MessageType.MEANS_ASSESSMENT_POST_PROCESSING,message);

        PostProcessing postProcessing = gson.fromJson(message, PostProcessing.class);

        log.info("Assessment Post-Processing Request Received for RepID: {}", postProcessing.getRepId());
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), postProcessing.getLaaTransactionId());

        maatIdValidator.validate(postProcessing.getRepId());
        postProcessingService.execute(postProcessing.getRepId());
        log.info("Assessment Post-Processing Request Complete");

    }
}
