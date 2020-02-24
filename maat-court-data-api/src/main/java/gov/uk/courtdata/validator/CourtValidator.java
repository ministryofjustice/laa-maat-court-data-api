package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import lombok.extern.slf4j.Slf4j;
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
    public Optional<Void> validate(final CaseDetails caseDetailsJson) throws ValidationException {

        Optional.ofNullable(caseDetailsJson.getCjsAreaCode())
                .orElseThrow(() -> new ValidationException("cjs area code is missing."));

        //TODO: No clarity yet on cjsCode at case level & multiple court location within sessions
        // to revisit and implement court/case area mismatch

        caseDetailsJson.getSessions().stream().forEach(s -> {
            Optional.ofNullable(s.getCourtLocation())
                    .orElseThrow(() -> new ValidationException("Court Location is missing from session "));
        });

        return Optional.empty();
    }

}
