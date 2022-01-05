package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import gov.uk.courtdata.validator.PassportAssessmentIdValidator;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@AllArgsConstructor
@Component
public class UpdatePassportAssessmentValidator implements IValidator<Void, UpdatePassportAssessment> {

    private final PassportAssessmentIdValidator passportAssessmentIdValidator;

    @Override
    public Optional<Void> validate(UpdatePassportAssessment passportAssessment) {
        passportAssessmentIdValidator.validate(passportAssessment.getId());
        if (isBlank(passportAssessment.getUserModified())) {
            throw new ValidationException("Username is required");
        }
        return Optional.empty();
    }
}
