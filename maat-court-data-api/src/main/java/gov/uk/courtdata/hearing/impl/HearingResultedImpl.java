package gov.uk.courtdata.hearing.impl;

import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.hearing.crowncourt.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.magistrate.service.MagistrateCourtService;
import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
@Slf4j
public class HearingResultedImpl {

    private final CrownCourtProcessingImpl crownCourtProcessingImpl;

    private final MagistrateCourtService magistrateCourtService;


    public void execute(HearingResulted hearingResulted) {

        switch (hearingResulted.getJurisdictionType()) {

            case CROWN:
                crownCourtProcessingImpl.execute(hearingResulted);
                break;
            case MAGISTRATES:
                magistrateCourtService.execute(hearingResulted);
                break;
            default:
                throw new MaatCourtDataException(format("Invalid Jurisdiction type %s",
                        hearingResulted.getJurisdictionType()));
        }

    }
}