package gov.uk.courtdata.validator;

import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LaaStatusValidationProcessor {

    private final MaatIdValidator maatIdValidator;
    private final LinkRegisterValidator linkRegisterValidator;
    private final SolicitorValidator solicitorValidator;
    private final LaaStatusValidator laaStatusValidator;
    private final DefendantValidator defendantValidator;


    public MessageCollection validate(CaseDetails caseDetails) {

        Integer maatId = caseDetails.getMaatId();
        maatIdValidator.validate(maatId);
        // TODO Validate CJS + Libra ID Combination here.
        linkRegisterValidator.validateMAATId(maatId);
        solicitorValidator.validate(caseDetails);
        defendantValidator.validate(maatId);

        return laaStatusValidator.validate(caseDetails);


    }
}
