package gov.uk.courtdata.hearing.validator;

import gov.uk.courtdata.model.hearing.HearingDetails;
import gov.uk.courtdata.validator.LinkRegisterValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HearingValidationProcessor {

    private final LinkRegisterValidator linkRegisterValidator;
    private final MaatIdValidator maatIdValidator;


    public void validate(HearingDetails hearingDetails) {

        Integer maatId = hearingDetails.getMaatId();
        maatIdValidator.validate(maatId);
        linkRegisterValidator.validate(maatId);
    }

}
