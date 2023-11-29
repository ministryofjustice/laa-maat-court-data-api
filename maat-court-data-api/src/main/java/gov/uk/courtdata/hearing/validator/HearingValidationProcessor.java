package gov.uk.courtdata.hearing.validator;

import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.validator.LinkRegisterValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HearingValidationProcessor {

    private final LinkRegisterValidator linkRegisterValidator;
    private final MaatIdValidator maatIdValidator;

    /**
     * @param hearingRes
     */
    public void validate(final HearingResulted hearingRes) {

        final Integer maatId = hearingRes.getMaatId();
        maatIdValidator.validate(maatId);
        linkRegisterValidator.validate(maatId);
        log.info("Hearing Validation Completed for MAAT ID: {}", maatId);
    }


}
