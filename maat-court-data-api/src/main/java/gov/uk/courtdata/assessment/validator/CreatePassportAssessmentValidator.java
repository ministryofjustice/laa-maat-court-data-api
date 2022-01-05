package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@AllArgsConstructor
@Component
public class CreatePassportAssessmentValidator implements IValidator<Void, CreatePassportAssessment> {

    @Override
    public Optional<Void> validate(CreatePassportAssessment createPassportAssessment) {

        if (isBlank(createPassportAssessment.getNworCode())) {
            throw new ValidationException("New work reason code is required");
        } else if (isBlank(createPassportAssessment.getUserCreated())) {
            throw new ValidationException("Username is required");
        } else if (isBlank(createPassportAssessment.getPastStatus())) {
            throw new ValidationException("Past Status is required");
        } if (createPassportAssessment.getFinancialAssessmentId() == null) {
            throw new ValidationException("Financial Assessment ID is required");
        }
        return Optional.empty();
    }
}
