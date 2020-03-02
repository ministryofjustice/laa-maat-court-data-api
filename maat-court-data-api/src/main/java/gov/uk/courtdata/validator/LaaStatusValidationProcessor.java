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
    private final DefendantValidator defendantValidator;


    public boolean validate(CaseDetails caseDetails, MessageCollection messageCollection) {


        Integer maatId = caseDetails.getMaatId();
        maatIdValidator.validate(maatId);
        // TODO Validate CJS + Libra ID Combination here.
        linkRegisterValidator.validateMAATId(maatId);
        solicitorValidator.validate(caseDetails);
        defendantValidator.validate(caseDetails);

        return true;
    }
}
