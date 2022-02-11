package gov.uk.courtdata.hearing.crowncourt;

import com.google.gson.Gson;
import gov.uk.courtdata.hearing.service.HearingResultedService;
import gov.uk.courtdata.prosecutionconcluded.model.ProsecutionConcluded;
import gov.uk.courtdata.prosecutionconcluded.service.ProsecutionConcludedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CrownTestController {
    private final Gson gson;
    private final HearingResultedService hearingResultedService;
    private final ProsecutionConcludedService prosecutionConcludedService;

//    @PostMapping("/crowncourt")
//    public void receive(@RequestBody String jsonPayloade) {
//        HearingResulted hearingResulted = gson.fromJson(jsonPayloade, HearingResulted.class);
//
//        hearingResultedService.execute(hearingResulted);
//
//    }
//
    @PostMapping("/concluded")
    public void concluded(@RequestBody String jsonPayloade) {
        ProsecutionConcluded prosecutionConcluded = gson.fromJson(jsonPayloade, ProsecutionConcluded.class);

        prosecutionConcludedService.execute(prosecutionConcluded);

    }
}