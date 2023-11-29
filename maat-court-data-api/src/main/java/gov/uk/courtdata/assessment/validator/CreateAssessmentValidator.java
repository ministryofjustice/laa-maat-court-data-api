package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Component
@AllArgsConstructor
public class CreateAssessmentValidator implements IValidator<Void, CreateFinancialAssessment> {

    @Override
    public Optional<Void> validate(CreateFinancialAssessment financialAssessment) {

        if (isBlank(financialAssessment.getNworCode())) {
            throw new ValidationException("New work reason code is required");
        } else if (isBlank(financialAssessment.getUserCreated())) {
            throw new ValidationException("Username is required");
        }
        return Optional.empty();
    }
}
