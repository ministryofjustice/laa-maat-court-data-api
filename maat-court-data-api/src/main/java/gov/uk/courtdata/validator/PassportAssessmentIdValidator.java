package gov.uk.courtdata.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class PassportAssessmentIdValidator implements IValidator<Void, Integer> {

    private final PassportAssessmentRepository passportAssessmentRepository;

    @Override
    public Optional<Void> validate(final Integer passportAssessmentId) {

        if (passportAssessmentId != null && passportAssessmentId > 0) {
            if (!passportAssessmentRepository.existsById(passportAssessmentId))
                throw new ValidationException(passportAssessmentId + " is invalid");
            return Optional.empty();

        } else {
            throw new ValidationException("Passport Assessment Id is required");
        }
    }
}
