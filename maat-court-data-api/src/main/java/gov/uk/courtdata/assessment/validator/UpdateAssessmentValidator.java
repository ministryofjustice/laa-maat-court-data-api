package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Component
@AllArgsConstructor
public class UpdateAssessmentValidator implements IValidator<Void, UpdateFinancialAssessment> {

    private final FinancialAssessmentIdValidator financialAssessmentIdValidator;

    @Override
    public Optional<Void> validate(UpdateFinancialAssessment financialAssessment) {
        financialAssessmentIdValidator.validate(financialAssessment.getId());
        if (isBlank(financialAssessment.getUserModified())) {
            throw new ValidationException("Username is required");
        }
        return Optional.empty();
    }
}
