package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.validator.IValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;


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

        if (isBlank(caseDetailsJson.getCjsAreaCode()))
            throw new ValidationException("cjs area code not found.");

        List<Session> sessions = caseDetailsJson.getSessions();

        if (sessions == null || sessions.isEmpty())
            throw new ValidationException("Sessions not available.");

        sessions.forEach(s ->
                Optional.ofNullable(s.getCourtLocation())
                        .orElseThrow(() -> new ValidationException("Court Location not available in session.")));


        return Optional.empty();
    }

}
