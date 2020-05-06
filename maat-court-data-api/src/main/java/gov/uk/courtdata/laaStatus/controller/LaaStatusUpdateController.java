package gov.uk.courtdata.laaStatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.laaStatus.service.LaaStatusPublisher;
import gov.uk.courtdata.laaStatus.validator.LaaStatusValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.LaaTransactionLogging;
import gov.uk.courtdata.model.MessageCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
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

        MessageCollection messageCollection = null;
        try {
            CaseDetails caseDetails = gson.fromJson(jsonPayload, CaseDetails.class);
            messageCollection = laaStatusValidationProcessor.validate(caseDetails);
            if (messageCollection.getMessages().isEmpty()) {

                log.info("Request Validation is successfully completed - laa-transaction-id:{}" , laaTransactionId );
                laaStatusPublisher.publish(caseDetails);
            } else {

                log(laaTransactionId, caseDetails);
                log.info("LAA Status Update Validation Failed - laa-transaction-id: {} - Messages {}", laaTransactionId, messageCollection.getMessages());
            }
        } catch (Exception exception) {
            assert messageCollection != null;
            messageCollection.getMessages().add(exception.getMessage());
            throw new MaatCourtDataException("MAAT APT Call failed " + exception.getMessage() + "laa-transaction-id" +laaTransactionId );
        }
        return messageCollection;
    }

    /**
     * Logging the object
     * @param laaTransactionId
     * @param caseDetails
     */
    private void log(String laaTransactionId, CaseDetails caseDetails) {
        LaaTransactionLogging logging = LaaTransactionLogging.builder()
                .maatId(caseDetails.getMaatId())
                .caseUrn(caseDetails.getCaseUrn())
                .laaTransactionId(Optional.ofNullable(UUID.fromString(laaTransactionId)).orElse(null))
                .build();
        log.info(logging.toString());
    }
}
