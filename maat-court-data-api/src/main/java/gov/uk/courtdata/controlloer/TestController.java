
package gov.uk.courtdata.controlloer;

import com.google.gson.Gson;
import gov.uk.courtdata.laaStatus.service.LaaStatusPostCDAService;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Unlink;
import gov.uk.courtdata.unlink.processor.UnLinkProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This class should be deleted.

@RestController
@RequiredArgsConstructor
@RequestMapping("maatApi")
public class TestController {

  //  private final SaveAndLinkProcessor saveAndLinkProcessor;
    private final UnLinkProcessor unlinkProcessor;

    private final LaaStatusPostCDAService laaStatusPostCDAService;
    private final Gson gson;

//    @PostMapping("/saveAndLink")
//    public String saveAndLink(@RequestBody String jsonPayload) {
//        saveAndLinkProcessor.process(jsonPayload);
//        return "Transaction has been linked successfully";
//    }

    @PostMapping("/unLink")
    public String unLink(@RequestBody String jsonPayload) {
        Unlink unlink = gson.fromJson(jsonPayload, Unlink.class);
        unlinkProcessor.process(unlink);
        return "Transaction has been unlinked successfully";
    }

    @PostMapping("/laaupdate")
    public String laaupdate(@RequestBody String jsonPayload) {
        CaseDetails unlink = gson.fromJson(jsonPayload, CaseDetails.class);
        laaStatusPostCDAService.process(unlink);
        return "Laa status posted to CDA successfully";
    }
}


