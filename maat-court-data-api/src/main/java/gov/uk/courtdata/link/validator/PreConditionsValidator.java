package gov.uk.courtdata.link.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.model.CaseDetailsValidate;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@XRayEnabled
@Component
@RequiredArgsConstructor
public class PreConditionsValidator {

    private final MaatIdValidator maatIdValidator;

    public void validate(final CaseDetailsValidate caseDetails) {

        final Integer maatId = caseDetails.getMaatId();

        maatIdValidator.validate(maatId);
    }
}