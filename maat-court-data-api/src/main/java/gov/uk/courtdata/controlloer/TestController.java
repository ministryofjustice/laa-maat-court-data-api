package gov.uk.courtdata.controlloer;

import gov.uk.courtdata.link.SaveAndLinkProcessor;
import gov.uk.courtdata.unlink.UnLinkProcessor;
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

    private final SaveAndLinkProcessor saveAndLinkProcessor;
    private final UnLinkProcessor unlinkProcessor;

    @PostMapping("/saveAndLink")
    public String saveAndLink(@RequestBody String jsonPayload) {
        saveAndLinkProcessor.process(jsonPayload);
        return "Transaction has been linked successfully";
    }

    @PostMapping("/unLink")
    public String unLink(@RequestBody String jsonPayload) {
        unlinkProcessor.process(jsonPayload);
        return "Transaction has been unlinked successfully";
    }
}

