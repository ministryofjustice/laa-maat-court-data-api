package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.validator.IValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 * <code>CourtValidator</code> to validate court code and court area for the case to  link.
 */
@Slf4j
@Component
public class CourtValidator implements IValidator<Void, CaseDetails> {


    /**
     * @param caseDetailsJson
     * @return
     * @throws ValidationException
     */
    @Override
    public Optional<Void> validate(final CaseDetails caseDetailsJson) {

        Optional.ofNullable(caseDetailsJson.getCjsAreaCode()).filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new ValidationException("cjs area code not found."));

        Optional.ofNullable(caseDetailsJson.getSessions())
                .orElseThrow(() -> new ValidationException("Sessions not available."));

        caseDetailsJson.getSessions().forEach(s ->
                Optional.ofNullable(s.getCourtLocation())
                        .orElseThrow(() -> new ValidationException("Court Location not available in session.")));


        return Optional.empty();
    }

}
