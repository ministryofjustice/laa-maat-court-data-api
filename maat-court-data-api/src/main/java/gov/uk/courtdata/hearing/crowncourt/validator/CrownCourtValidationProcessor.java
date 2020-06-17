package gov.uk.courtdata.hearing.crowncourt.validator;

import gov.uk.courtdata.model.hearing.HearingResulted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CrownCourtValidationProcessor {

    private final CrownCourtOutComesValidator crownCourtOutComesValidator;
    private final AppealTypeValidator appealTypeValidator;
    private final OUCodeValidator ouCodeValidator;


    public void validate(final HearingResulted hearingRes) {

        crownCourtOutComesValidator.validate(hearingRes);
        appealTypeValidator.validate(hearingRes);
        ouCodeValidator.validate(hearingRes);
        log.info("Crown Court Outcome Validation has been Completed for MAAT ID: {}", hearingRes.getMaatId());

    }

}
