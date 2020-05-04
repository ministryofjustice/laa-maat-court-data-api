package gov.uk.courtdata.hearing.service;

import gov.uk.courtdata.exception.MaatCourtDataException;
import gov.uk.courtdata.hearing.crowncourt.CrownCourtProcessingImpl;
import gov.uk.courtdata.hearing.magistrate.service.MagistrateCourtService;
import gov.uk.courtdata.hearing.validator.CrownCourtValidationProcessor;
import gov.uk.courtdata.hearing.validator.MagistrateValidationProcessor;
import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class HearingResultedService {

    private final MagistrateValidationProcessor magsValidationProcessor;

    private final MagistrateCourtService magistrateCourtService;

    private final CrownCourtProcessingImpl crownCourtProcessingImpl;

    private final CrownCourtValidationProcessor crownValidationProcessor;


    /**
     * @param hearingResulted
     */
    public void process(final HearingResulted hearingResulted) {


        switch (hearingResulted.getJurisdictionType()) {

            case CROWN:
                crownValidationProcessor.validate(hearingResulted);
                crownCourtProcessingImpl.execute(hearingResulted);
                break;
            case MAGISTRATES:
                magsValidationProcessor.validate(hearingResulted);
                magistrateCourtService.execute(hearingResulted);
                break;
            default:
                throw new MaatCourtDataException(format("Invalid Jurisdiction type %s",
                        hearingResulted.getJurisdictionType()));
        }

    }

}
