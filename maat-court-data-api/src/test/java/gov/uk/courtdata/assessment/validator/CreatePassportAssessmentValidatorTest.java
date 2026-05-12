package gov.uk.courtdata.assessment.validator;

import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatePassportAssessmentValidatorTest {

    @InjectMocks
    private CreatePassportAssessmentValidator createAssessmentValidator;

    private CreatePassportAssessment getAssessmentWithUserCreated(String userCreated) {
        CreatePassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setUserCreated(userCreated);
        return assessment;
    }

    @Test
    void testCreatePassportAssessmentValidator_whenFinancialAssessmentIdIsFilled_thenValidationPasses() {
        Optional<Void> result = createAssessmentValidator.validate(getAssessmentWithUserCreated("test-f"));
        assertThat(result).isNotPresent();
    }

    @Test
    void testCreatePassportAssessmentValidator_whenFinancialAssessmentIdIsNotFilled_thenThrowsException() {
        CreatePassportAssessment createPassportAssessment = getAssessmentWithUserCreated("test-f");
        createPassportAssessment.setFinancialAssessmentId(null);
        ValidationException validationException = Assertions.assertThrows(
                ValidationException.class, () -> createAssessmentValidator.validate(createPassportAssessment));
        assertThat(validationException.getMessage()).isEqualTo("Financial Assessment ID is required");
    }

    @Test
    void testCreatePassportAssessmentValidator_whenUserCreatedIsBlank_thenThrowsException() {
        var assessment = getAssessmentWithUserCreated("");
        ValidationException validationException = Assertions.assertThrows(
                ValidationException.class, () -> createAssessmentValidator.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }

    @Test
    void testCreatePassportAssessmentValidator_whenUserCreatedIsNull_thenThrowsException() {
        var assessment = getAssessmentWithUserCreated(null);
        ValidationException validationException = Assertions.assertThrows(
                ValidationException.class, () -> createAssessmentValidator.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }
}
