package gov.uk.courtdata.laastatus.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import gov.uk.courtdata.validator.DefendantValidator;
import gov.uk.courtdata.validator.LinkRegisterValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import gov.uk.courtdata.validator.SolicitorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@XRayEnabled
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
        linkRegisterValidator.validate(maatId);
        solicitorValidator.validate(maatId);
        defendantValidator.validate(maatId);

        return laaStatusValidator.validate(caseDetails);


    }
}
