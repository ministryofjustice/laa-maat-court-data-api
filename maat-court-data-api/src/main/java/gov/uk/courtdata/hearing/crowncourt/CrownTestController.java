package gov.uk.courtdata.hearing.crowncourt;

import com.google.gson.Gson;
import gov.uk.courtdata.hearing.service.HearingResultedService;
import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CrownTestController {
    private final Gson gson;
    private final HearingResultedService hearingResultedService;
    @PostMapping("/crowncourt")
    public void receive(@RequestBody String jsonPayloade)  {
        HearingResulted hearingResulted = gson.fromJson(jsonPayloade, HearingResulted.class);
        hearingResultedService.execute(hearingResulted);
    }
}
