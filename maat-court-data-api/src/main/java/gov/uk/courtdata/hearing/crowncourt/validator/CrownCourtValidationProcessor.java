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
    private final OUCodeValidator ouCodeValidator;
    private final CaseTypeValidator caseTypeValidator;


    public void validate(final HearingResulted hearingRes) {

        crownCourtOutComesValidator.validate(hearingRes);
        ouCodeValidator.validate(hearingRes);
        caseTypeValidator.validate(hearingRes);
        log.info("Crown Court Outcome Validation has been Completed for MAAT ID: {}", hearingRes.getMaatId());
    }

}
