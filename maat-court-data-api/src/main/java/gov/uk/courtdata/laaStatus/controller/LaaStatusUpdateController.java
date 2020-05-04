package gov.uk.courtdata.laaStatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.laaStatus.service.LaaStatusPublisher;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.laaStatus.validator.LaaStatusValidationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("maatApi")
public class LaaStatusUpdateController {

    private final LaaStatusValidationProcessor laaStatusValidationProcessor;
    private final Gson gson;
    private final LaaStatusPublisher laaStatusPublisher;

    @PostMapping("/laaStatus")
    public MessageCollection updateLAAStatus(@RequestBody String jsonPayload) {

        log.info("LAA Status Update Request received. Message :  {}", jsonPayload);

        MessageCollection messageCollection = null;
        try {
            CaseDetails caseDetails = gson.fromJson(jsonPayload, CaseDetails.class);
            messageCollection = laaStatusValidationProcessor.validate(caseDetails);
            if (messageCollection.getMessages().isEmpty()) {
                log.info("Request Validation is successfully completed");
                laaStatusPublisher.publish(caseDetails);
            } else {
                log.info("LAA Status Update Validation Failed - {}", messageCollection.getMessages());
            }
        } catch (Exception exception) {
            assert messageCollection != null;
            messageCollection.getMessages().add(exception.getMessage());
            throw new MaatCourtDataException("MAAT APT Call failed" + exception.getMessage());
        }
        return messageCollection;
    }
}
