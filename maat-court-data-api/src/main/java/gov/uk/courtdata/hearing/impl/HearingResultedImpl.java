package gov.uk.courtdata.hearing.impl;

import gov.uk.courtdata.hearing.magistrate.service.MagistrateCourtService;
import gov.uk.courtdata.hearing.model.HearingResulted;
import gov.uk.courtdata.hearing.crowncourt.CrownCourtProcessingImpl;
import gov.uk.courtdata.model.hearing.HearingDetails;
import gov.uk.courtdata.hearing.magistrate.service.MagistrateCourtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.CROWN_COURT;

@Component
@RequiredArgsConstructor
@Slf4j
public class HearingResultedImpl {

    private final CrownCourtProcessingImpl crownCourtProcessingImpl;

    private final MagistrateCourtService magistrateCourtService;

    public void execute(CourtDataDTO courtDataDTO){
        if (CROWN_COURT.equalsIgnoreCase(hearingDetails.getJurisdictionType())) {
            crownCourtProcessingImpl.execute(hearingDetails);
        }    }

         magistrateCourtService.execute(hearingResulted);
}
