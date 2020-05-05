package gov.uk.courtdata.hearing.impl;

import gov.uk.courtdata.hearing.crowncourt.CrownCourtProcessingImpl;
import gov.uk.courtdata.model.hearing.HearingDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static gov.uk.courtdata.constants.CourtDataConstants.CROWN_COURT;

@Component
@RequiredArgsConstructor
@Slf4j
public class HearingResultedImpl {

    private final CrownCourtProcessingImpl crownCourtProcessingImpl;

    public void execute(HearingDetails hearingDetails) {

        if (CROWN_COURT.equalsIgnoreCase(hearingDetails.getJurisdictionType())) {
            crownCourtProcessingImpl.execute(hearingDetails);
        }
    }
}
