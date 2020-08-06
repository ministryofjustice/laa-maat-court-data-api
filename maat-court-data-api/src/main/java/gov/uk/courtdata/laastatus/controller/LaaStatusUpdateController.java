package gov.uk.courtdata.laastatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.laastatus.service.LaaStatusPublisher;
import gov.uk.courtdata.laastatus.validator.LaaStatusValidationProcessor;
import gov.uk.courtdata.model.CaseDetails;
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
    public MessageCollection updateLAAStatus(@RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId, @RequestBody String jsonPayload) {

        log.info("LAA Status Update Request received.");
        MessageCollection messageCollection;
        try {
            CaseDetails caseDetails = gson.fromJson(jsonPayload, CaseDetails.class);

            UUID ud = Optional.ofNullable(laaTransactionId).isPresent() ? UUID.fromString(laaTransactionId) : UUID.randomUUID();
            caseDetails.setLaaTransactionId(ud);

            messageCollection = laaStatusValidationProcessor.validate(caseDetails);

            if (messageCollection.getMessages().isEmpty()) {
                log.info("Request Validation is successfully completed.");
                laaStatusPublisher.publish(caseDetails);
            } else {
                log.info("LAA Status Update Validation Failed - Messages {}", messageCollection.getMessages());
            }
        } catch (Exception exception) {
            throw new MAATCourtDataException("MAAT APT Call failed " + exception.getMessage());
        }
        return messageCollection;
    }
}
