package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.validator.IValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinancialAssessmentIdValidator implements IValidator<Void, Integer> {

    private final FinancialAssessmentRepository financialAssessmentRepository;

    @Override
    public Optional<Void> validate(final Integer assessmentId) {

        if (assessmentId != null && assessmentId > 0) {
            if (!financialAssessmentRepository.existsById(assessmentId))
                throw new ValidationException(assessmentId + " is invalid");
            return Optional.empty();

        } else {
            throw new ValidationException("Financial Assessment id is required");
        }
    }
}
