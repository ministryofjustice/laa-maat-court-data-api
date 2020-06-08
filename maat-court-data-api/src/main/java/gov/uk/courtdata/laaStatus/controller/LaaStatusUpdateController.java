package gov.uk.courtdata.laaStatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.laaStatus.service.LaaStatusPublisher;
import gov.uk.courtdata.laaStatus.validator.LaaStatusValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.util.LaaTransactionLoggingBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("maatApi")
public class LaaStatusUpdateController {

    private final LaaStatusValidationProcessor laaStatusValidationProcessor;
    private final Gson gson;
    private final LaaStatusPublisher laaStatusPublisher;

    @PostMapping("/laaStatus")
    public MessageCollection updateLAAStatus(@RequestHeader("Laa-Transaction-Id") String laaTransactionId,@RequestBody String jsonPayload) {

        log.info("LAA Status Update Request received - laa-transaction-id:{}" , laaTransactionId);
        String laaLogging = LaaTransactionLoggingBuilder.getStr(jsonPayload);
        MessageCollection messageCollection = null;
        try {
            CaseDetails caseDetails = gson.fromJson(jsonPayload, CaseDetails.class);
            caseDetails.setLaaTransactionId(UUID.fromString(laaTransactionId));
            messageCollection = laaStatusValidationProcessor.validate(caseDetails);

            if (messageCollection.getMessages().isEmpty()) {
                log.info("Request Validation is successfully completed: {}" , laaLogging );
                laaStatusPublisher.publish(caseDetails);
            } else {
                log.info("LAA Status Update Validation Failed - Messages {} - {}", messageCollection.getMessages(),laaLogging);
            }
        } catch (Exception exception) {
            assert messageCollection != null;
            messageCollection.getMessages().add(exception.getMessage());
            throw new MAATCourtDataException("MAAT APT Call failed " + exception.getMessage() + "laa-logging" +laaLogging);
        }
        return messageCollection;
    }
}
