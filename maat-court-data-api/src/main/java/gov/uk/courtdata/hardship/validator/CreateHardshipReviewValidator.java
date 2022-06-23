package gov.uk.courtdata.hardship.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
@XRayEnabled
public class CreateHardshipReviewValidator implements IValidator<Void, CreateHardshipReview> {

    private final FinancialAssessmentRepository financialAssessmentRepository;

    @Override
    public Optional<Void> validate(final CreateHardshipReview createHardshipReview) {
        Optional<FinancialAssessmentEntity> assessment =
                financialAssessmentRepository.findCompletedAssessmentByRepId(createHardshipReview.getRepId());
        if (assessment.isEmpty()) {
            throw new ValidationException("Review can only be entered after a completed assessment");
        }

        LocalDateTime reviewDate = createHardshipReview.getReviewDate();
        LocalDateTime fullAssessmentDate = assessment.get().getFullAssessmentDate();
        LocalDateTime initialAssessmentDate = assessment.get().getInitialAssessmentDate();
        LocalDateTime timestamp = fullAssessmentDate != null ? fullAssessmentDate : initialAssessmentDate;

        if (reviewDate.toLocalDate().isBefore(timestamp.toLocalDate())) {
            throw new ValidationException("Review date cannot pre-date the means assessment date");
        }
        return Optional.empty();
    }
}
