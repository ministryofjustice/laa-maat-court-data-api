package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CreatePassportAssessmentValidatorTest {

    @InjectMocks
    private CreatePassportAssessmentValidator createAssessmentValidator;

    private CreatePassportAssessment getAssessmentWithUserCreated(String userCreated) {
        CreatePassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setUserCreated(userCreated);
        return assessment;
    }

    @Test
    public void testCreatePassportAssessmentValidator_whenFinancialAssessmentIdIsFilled_thenValidationPasses() {
        Optional<Void> result = createAssessmentValidator.validate(getAssessmentWithUserCreated("test-f"));
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void testCreatePassportAssessmentValidator_whenFinancialAssessmentIdIsNotFilled_thenThrowsException() {
        CreatePassportAssessment createPassportAssessment = getAssessmentWithUserCreated("test-f");
        createPassportAssessment.setFinancialAssessmentId(null);
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> createAssessmentValidator.validate(createPassportAssessment));
        assertThat(validationException.getMessage()).isEqualTo("Financial Assessment ID is required");
    }

    @Test
    public void testCreatePassportAssessmentValidator_whenUserCreatedIsFilled_thenValidationPasses() {
        Optional<Void> result = createAssessmentValidator.validate(getAssessmentWithUserCreated("test-f"));
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void testCreatePassportAssessmentValidator_whenUserCreatedIsBlank_thenThrowsException() {
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> createAssessmentValidator.validate(getAssessmentWithUserCreated("")));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }

    @Test
    public void testCreatePassportAssessmentValidator_whenUserCreatedIsNull_thenThrowsException() {
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> createAssessmentValidator.validate(getAssessmentWithUserCreated(null)));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }
}
