package gov.uk.courtdata.assessment.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.UpdateFinancialAssessment;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateAssessmentValidatorTest {

    @InjectMocks
    private UpdateAssessmentValidator updateAssessmentValidator;

    @Mock
    private FinancialAssessmentIdValidator financialAssessmentIdValidator;

    @BeforeEach
    void setUp() {
        when(financialAssessmentIdValidator.validate(any())).thenReturn(Optional.empty());
    }

    @Test
    void testUpdateAssessmentValidator_whenUserModifiedIsBlank_thenThrowsException() {
        UpdateFinancialAssessment mockFinancialAssessment =
                UpdateFinancialAssessment.builder().userModified("").build();
        ValidationException validationException = Assertions.assertThrows(
                ValidationException.class, () -> updateAssessmentValidator.validate(mockFinancialAssessment));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }

    @Test
    void testUpdateAssessmentValidator_whenUserModifiedIsNull_thenThrowsException() {
        UpdateFinancialAssessment mockFinancialAssessment =
                UpdateFinancialAssessment.builder().userModified(null).build();
        ValidationException validationException = Assertions.assertThrows(
                ValidationException.class, () -> updateAssessmentValidator.validate(mockFinancialAssessment));
        assertThat(validationException.getMessage()).isEqualTo("Username is required");
    }

    @Test
    void testUpdateAssessmentValidator_whenUserModifiedIsFilled_thenValidationPasses() {
        UpdateFinancialAssessment mockFinancialAssessment =
                UpdateFinancialAssessment.builder().userModified("test-f").build();
        Optional<Void> result = updateAssessmentValidator.validate(mockFinancialAssessment);
        assertThat(result).isNotPresent();
    }
}
