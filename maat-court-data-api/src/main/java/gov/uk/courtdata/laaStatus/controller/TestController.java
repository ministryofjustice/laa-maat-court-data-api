package gov.uk.courtdata.laaStatus.controller;

import com.google.gson.Gson;
import gov.uk.courtdata.laaStatus.builder.CourtDataDTOBuilder;
import gov.uk.courtdata.laaStatus.service.LaaStatusPostCDAService;
import gov.uk.courtdata.model.CaseDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


// This class should be deleted.

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
@Slf4j
public class TestController {


    private final Gson gson;

    private final LaaStatusPostCDAService laaStatusPostCDAService;

    private final CourtDataDTOBuilder courtDataDTOBuilder;

//    @PostMapping("/saveAndLink")
//    public String saveAndLink(@RequestBody String jsonPayload) {
//        saveAndLinkProcessor.process(jsonPayload);
//        return "Transaction has been linked successfully";
//    }

    @PostMapping("/laaupdate")
    public String unLink(@RequestBody String jsonPayload) {
        CaseDetails unlink = gson.fromJson(jsonPayload, CaseDetails.class);
        log.info("json posted" + unlink.toString());
        laaStatusPostCDAService.process(courtDataDTOBuilder.build(unlink));
        return "Transaction has been unlinked successfully";
    }
}


