package gov.uk.courtdata.laaStatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.laaStatus.service.LaaStatusPublisher;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.validator.LaaStatusValidationProcessor;
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

        log.debug("LAA Status Update Request received. Message :  {}", jsonPayload);

        CaseDetails caseDetails = gson.fromJson(jsonPayload, CaseDetails.class);

        MessageCollection messageCollection = laaStatusValidationProcessor.validate(caseDetails);

        if (messageCollection.getMessages().isEmpty()) {
            log.debug("Request Validation is successfully completed");
            laaStatusPublisher.publish(caseDetails);
        } else {
            log.debug("LAA Status Update Validation Failed - {}", messageCollection.getMessages());
        }
        return messageCollection;
    }
}
