package gov.uk.courtdata.hearing.impl;

import gov.uk.courtdata.hearing.magistrate.service.MagistrateCourtService;
import gov.uk.courtdata.hearing.model.HearingResulted;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HearingResultedImpl {

    private final MagistrateCourtService magistrateCourtService;

    public void execute(final HearingResulted hearingResulted){
        magistrateCourtService.execute(hearingResulted);

    }



}
