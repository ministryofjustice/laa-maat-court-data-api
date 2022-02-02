package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.*;
import gov.uk.courtdata.validator.PassportAssessmentIdValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class PassportAssessmentValidationProcessor {
    private final CreatePassportAssessmentValidator createPassportAssessmentValidator;
    private final UpdatePassportAssessmentValidator updatePassportAssessmentValidator;
    private final PassportAssessmentIdValidator passportAssessmentIdValidator;

    public Optional<Void> validate(Integer passportAssessmentId) {
        return passportAssessmentIdValidator.validate(passportAssessmentId);
    }

    public Optional<Void> validate(PassportAssessment passportAssessment) {

        if (passportAssessment == null) {
            throw new ValidationException("Passport Assessment Request is empty");
        } else if (passportAssessment.getRepId() == null) {
            throw new ValidationException("Rep Order ID is required");
        } else if (passportAssessment.getCmuId() == null) {
            throw new ValidationException("Case Management Unit (CMU) ID is required");
        } else if (StringUtils.isBlank(passportAssessment.getNworCode())) {
            throw new ValidationException("New Work Reason (NWOR) code is required");
        } else if (isBlank(passportAssessment.getPastStatus())) {
            throw new ValidationException("Past Status is required");
        }
        if (passportAssessment instanceof CreatePassportAssessment) {
            return createPassportAssessmentValidator.validate((CreatePassportAssessment) passportAssessment);
        }
        if (passportAssessment instanceof UpdatePassportAssessment) {
            return updatePassportAssessmentValidator.validate((UpdatePassportAssessment) passportAssessment);
        }
        return Optional.empty();
    }
}
