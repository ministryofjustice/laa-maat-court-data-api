package gov.uk.courtdata.hearing.validator;

import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.validator.LinkRegisterValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MagistrateValidationProcessor {

    private final LinkRegisterValidator linkRegisterValidator;
    private final MaatIdValidator maatIdValidator;

    /**
     *
     * @param hearingRes
     */
    public void validate(final HearingResulted hearingRes) {

        maatIdValidator.validate(hearingRes.getMaatId());
        linkRegisterValidator.validate(hearingRes.getMaatId());
    }


}
