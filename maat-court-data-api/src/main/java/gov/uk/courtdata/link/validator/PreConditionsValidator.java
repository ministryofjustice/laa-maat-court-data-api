package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.CaseDetailsValidate;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreConditionsValidator {

    private final MaatIdValidator maatIdValidator;

    private final CPDataValidator cpDataValidator;

    private final LinkExistsValidator linkExistsValidator;


    public void validate(final CaseDetailsValidate caseDetails) {

        final Integer maatId = caseDetails.getMaatId();

        maatIdValidator.validate(maatId);
        linkExistsValidator.validate(maatId);
        cpDataValidator.validate(CaseDetails.builder().caseUrn(caseDetails.getCaseUrn()).maatId(maatId).build());

    }
}
