package gov.uk.courtdata.laaStatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.laaStatus.service.LaaStatusUpdateService;
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
    private final LaaStatusUpdateService laaStatusUpdateService;

    @PostMapping("/laaStatus")
    public MessageCollection updateLAAStatus(@RequestBody String jsonPayload) {

        log.debug("LAA Status Update Request received. Message :  {}", jsonPayload);

        CaseDetails caseDetails = gson.fromJson(jsonPayload, CaseDetails.class);

        CourtDataDTO courtDataDTO = laaStatusValidationProcessor.validate(caseDetails);
        MessageCollection messageCollection = courtDataDTO.getMessageCollection();
        if (!messageCollection.getMessages().isEmpty()) {
            laaStatusUpdateService.execute(courtDataDTO);
        } else {
            log.debug("LAA Status Update Validation Failed - {}", messageCollection.getMessages());
        }
        return messageCollection;
    }
}
