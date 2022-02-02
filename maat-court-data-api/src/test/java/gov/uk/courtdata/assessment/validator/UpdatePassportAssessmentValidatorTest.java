package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import gov.uk.courtdata.validator.PassportAssessmentIdValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdatePassportAssessmentValidatorTest {

    @InjectMocks
    private UpdatePassportAssessmentValidator updatePassportAssessmentValidator;

    @Mock
    private PassportAssessmentIdValidator passportAssessmentIdValidator;

    @Before
    public void setUp() {
        when(passportAssessmentIdValidator.validate(any())).thenReturn(Optional.empty());
    }

    @Test
    public void testUpdateAssessmentValidator_whenUserModifiedIsBlank_thenThrowsException() {
        UpdatePassportAssessment mockPassportAssessment =
                UpdatePassportAssessment.builder().userModified("").build();
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> updatePassportAssessmentValidator.validate(mockPassportAssessment));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }

    @Test
    public void testUpdateAssessmentValidator_whenUserModifiedIsNull_thenThrowsException() {
        UpdatePassportAssessment mockPassportAssessment =
                UpdatePassportAssessment.builder().userModified(null).build();
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> updatePassportAssessmentValidator.validate(mockPassportAssessment));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }

    @Test
    public void testUpdateAssessmentValidator_whenUserModifiedIsFilled_thenValidationPasses() {
        UpdatePassportAssessment mockPassportAssessment =
                UpdatePassportAssessment.builder().userModified("test-f").build();
        Optional<Void> result = updatePassportAssessmentValidator.validate(mockPassportAssessment);
        assertThat(result).isEqualTo(Optional.empty());
    }
}
