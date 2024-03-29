package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.PassportAssessment;
import gov.uk.courtdata.validator.PassportAssessmentIdValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassportAssessmentValidationProcessorTest {

    @InjectMocks
    private PassportAssessmentValidationProcessor passportAssessmentValidationProcessor;

    @Mock
    private CreatePassportAssessmentValidator createPassportAssessmentValidator;

    @Mock
    private PassportAssessmentIdValidator passportAssessmentIdValidator;

    @Test
    public void testPassportAssessmentValidationProcessor_whenIdIsPassed_thenCallsIdValidator() {
        when(passportAssessmentIdValidator.validate(any(Integer.class))).thenReturn(Optional.empty());
        passportAssessmentValidationProcessor.validate(1000);
        verify(passportAssessmentIdValidator, times(1)).validate(1000);
    }

    @Test
    public void testPassportAssessmentValidationProcessor_whenAssessmentIsNull_thenThrowsException() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> passportAssessmentValidationProcessor.validate((PassportAssessment) null));
        assertThat(validationException.getMessage()).isEqualTo("Passport Assessment Request is empty");
    }

    @Test
    public void testPassportAssessmentValidationProcessor_whenRepIdIsNull_thenThrowsException() {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setRepId(null);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> passportAssessmentValidationProcessor.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Rep Order ID is required");
    }

    @Test
    public void testPassportAssessmentValidationProcessor_whenCmuIdIsNull_thenThrowsException() {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setCmuId(null);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> passportAssessmentValidationProcessor.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Case Management Unit (CMU) ID is required");
    }

    @Test
    public void testPassportAssessmentValidationProcessor_whenNWORCodeIsNull_thenThrowsException() {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setNworCode(null);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> passportAssessmentValidationProcessor.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("New Work Reason (NWOR) code is required");
    }
    @Test
    public void testPassportAssessmentValidationProcessor_whenNWORCodeIsBlank_thenThrowsException() {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setNworCode("");
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> passportAssessmentValidationProcessor.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("New Work Reason (NWOR) code is required");
    }

    @Test
    public void testPassportAssessmentValidationProcessor_whenPastStatusIsNull_thenThrowsException() {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setPastStatus(null);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> passportAssessmentValidationProcessor.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Past Status is required");
    }

    @Test
    public void testPassportAssessmentValidationProcessor_whenPastStatusIsBlank_thenThrowsException() {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        assessment.setPastStatus("");
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> passportAssessmentValidationProcessor.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Past Status is required");
    }

    @Test
    public void testPassportAssessmentValidationProcessor_whenRequiredFieldsPresent_thenValidationPasses() {
        PassportAssessment assessment = TestModelDataBuilder.getCreatePassportAssessment();
        when(createPassportAssessmentValidator.validate(any(CreatePassportAssessment.class))).thenReturn(Optional.empty());
        assertThat(passportAssessmentValidationProcessor.validate(assessment)).isEqualTo(Optional.empty());
    }

}
