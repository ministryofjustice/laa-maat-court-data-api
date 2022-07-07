package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateAssessmentValidatorTest {

    @InjectMocks
    private UpdateAssessmentValidator updateAssessmentValidator;

    @Mock
    private FinancialAssessmentIdValidator financialAssessmentIdValidator;

    @BeforeEach
    public void setUp() {
        when(financialAssessmentIdValidator.validate(any())).thenReturn(Optional.empty());
    }

    @Test
    public void testUpdateAssessmentValidator_whenUserModifiedIsBlank_thenThrowsException() {
        UpdateFinancialAssessment mockFinancialAssessment =
                UpdateFinancialAssessment.builder().userModified("").build();
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> updateAssessmentValidator.validate(mockFinancialAssessment));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }

    @Test
    public void testUpdateAssessmentValidator_whenUserModifiedIsNull_thenThrowsException() {
        UpdateFinancialAssessment mockFinancialAssessment =
                UpdateFinancialAssessment.builder().userModified(null).build();
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> updateAssessmentValidator.validate(mockFinancialAssessment));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }

    @Test
    public void testUpdateAssessmentValidator_whenUserModifiedIsFilled_thenValidationPasses() {
        UpdateFinancialAssessment mockFinancialAssessment =
                UpdateFinancialAssessment.builder().userModified("test-f").build();
        Optional<Void> result = updateAssessmentValidator.validate(mockFinancialAssessment);
        assertThat(result).isEqualTo(Optional.empty());
    }
}
