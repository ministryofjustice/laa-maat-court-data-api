package gov.uk.courtdata.assessment.validator;

import static org.assertj.core.api.Assertions.assertThat;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateAssessmentValidatorTest {

    @InjectMocks
    private CreateAssessmentValidator createAssessmentValidator;

    private CreateFinancialAssessment getAssessmentWithNworCode(String nworCode) {
        CreateFinancialAssessment assessment = TestModelDataBuilder.getCreateFinancialAssessment();
        assessment.setNworCode(nworCode);
        return assessment;
    }

    private CreateFinancialAssessment getAssessmentWithUserCreated(String userCreated) {
        CreateFinancialAssessment assessment = TestModelDataBuilder.getCreateFinancialAssessment();
        assessment.setUserCreated(userCreated);
        return assessment;
    }

    @Test
    void testCreateAssessmentValidator_whenNworCodeIsFilled_thenValidationPasses() {
        Optional<Void> result = createAssessmentValidator.validate(getAssessmentWithNworCode("1000"));
        assertThat(result).isNotPresent();
    }

    @Test
    void testCreateAssessmentValidator_whenNworCodeIsBlank_thenThrowsException() {
        var assessment = getAssessmentWithNworCode("");
        ValidationException validationException = Assertions.assertThrows(
                ValidationException.class, () -> createAssessmentValidator.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("New work reason code is required");
    }

    @Test
    void testCreateAssessmentValidator_whenNworCodeIsNull_thenThrowsException() {
        var assessment = getAssessmentWithNworCode(null);
        ValidationException validationException = Assertions.assertThrows(
                ValidationException.class, () -> createAssessmentValidator.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("New work reason code is required");
    }

    @Test
    void testCreateAssessmentValidator_whenUserCreatedIsFilled_thenValidationPasses() {
        Optional<Void> result = createAssessmentValidator.validate(getAssessmentWithUserCreated("test-f"));
        assertThat(result).isNotPresent();
    }

    @Test
    void testCreateAssessmentValidator_whenUserCreatedIsBlank_thenThrowsException() {
        var assessment = getAssessmentWithUserCreated("");
        ValidationException validationException = Assertions.assertThrows(
                ValidationException.class, () -> createAssessmentValidator.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }

    @Test
    void testCreateAssessmentValidator_whenUserCreatedIsNull_thenThrowsException() {
        var assessment = getAssessmentWithUserCreated(null);
        ValidationException validationException = Assertions.assertThrows(
                ValidationException.class, () -> createAssessmentValidator.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }
}
