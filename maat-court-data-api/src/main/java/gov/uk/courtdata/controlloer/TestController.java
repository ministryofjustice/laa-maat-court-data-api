package gov.uk.courtdata.controlloer;

import gov.uk.courtdata.link.SaveAndLinkProcessor;
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

    @PostMapping("/saveAndLink")
    public String saveAndLink(@RequestBody String jsonPayload) {
        saveAndLinkProcessor.process(jsonPayload);
        return "Transaction has been linked successfully";
    }
}

