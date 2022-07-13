package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class CreateAssessmentValidatorTest {

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
    public void testCreateAssessmentValidator_whenNworCodeIsFilled_thenValidationPasses() {
        Optional<Void> result = createAssessmentValidator.validate(getAssessmentWithNworCode("1000"));
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void testCreateAssessmentValidator_whenNworCodeIsBlank_thenThrowsException() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> createAssessmentValidator.validate(getAssessmentWithNworCode("")));
        assertThat(validationException.getMessage()).isEqualTo("New work reason code is required");
    }

    @Test
    public void testCreateAssessmentValidator_whenNworCodeIsNull_thenThrowsException() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> createAssessmentValidator.validate(getAssessmentWithNworCode(null)));
        assertThat(validationException.getMessage()).isEqualTo("New work reason code is required");
    }

    @Test
    public void testCreateAssessmentValidator_whenUserCreatedIsFilled_thenValidationPasses() {
        Optional<Void> result = createAssessmentValidator.validate(getAssessmentWithUserCreated("test-f"));
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void testCreateAssessmentValidator_whenUserCreatedIsBlank_thenThrowsException() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> createAssessmentValidator.validate(getAssessmentWithUserCreated("")));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }

    @Test
    public void testCreateAssessmentValidator_whenUserCreatedIsNull_thenThrowsException() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> createAssessmentValidator.validate(getAssessmentWithUserCreated(null)));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }
}
