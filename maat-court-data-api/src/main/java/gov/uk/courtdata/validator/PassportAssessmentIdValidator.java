package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class PassportAssessmentIdValidator implements IValidator<Void, Integer> {

    private final PassportAssessmentRepository passportAssessmentRepository;

    @Override
    public Optional<Void> validate(final Integer passportAssessmentId) {

        if (passportAssessmentId != null && passportAssessmentId > 0) {
            Optional<PassportAssessmentEntity> passportAssessmentEntity = passportAssessmentRepository.findById(passportAssessmentId);
            if (passportAssessmentEntity.isEmpty())
                throw new ValidationException(passportAssessmentId + " is invalid");
            return Optional.empty();

        } else {
            throw new ValidationException("Passport Assessment Id is required");
        }

    }
}