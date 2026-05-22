package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.assessment.service.OutstandingAssessmentService;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.FinancialAssessment;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FinancialAssessmentValidationProcessor {
    private final CreateAssessmentValidator createAssessmentValidator;
    private final UpdateAssessmentValidator updateAssessmentValidator;
    private final FinancialAssessmentIdValidator financialAssessmentIdValidator;
    private final OutstandingAssessmentService outstandingAssessmentService;

    public Optional<Void> validate(Integer financialAssessmentId) {
        return financialAssessmentIdValidator.validate(financialAssessmentId);
    }

    public Optional<Void> validate(FinancialAssessment financialAssessment) {

        if (financialAssessment == null) {
            throw new ValidationException("Financial Assessment Request is empty");
        } else if (financialAssessment.getRepId() == null) {
            throw new ValidationException("Rep Order ID is required");
        } else if (financialAssessment.getInitialAscrId() == null) {
            throw new ValidationException("Assessment Criteria ID is required");
        } else if (financialAssessment.getCmuId() == null) {
            throw new ValidationException("Case management unit ID is required");
        }

        if (financialAssessment instanceof CreateFinancialAssessment createAssessment) {
            createAssessmentValidator.validate(createAssessment);
            outstandingAssessmentService
                    .checkForOutstandingAssessments(createAssessment.getRepId())
                    .ifPresent(message -> {
                        throw new ValidationException(message.message());
                    });
        }
        if (financialAssessment instanceof UpdateFinancialAssessment updateAssessment) {
            updateAssessmentValidator.validate(updateAssessment);
        }
        return Optional.empty();
    }
}
