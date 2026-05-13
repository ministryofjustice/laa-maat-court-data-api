package gov.uk.courtdata.assessment.validator;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FinancialAssessmentIdValidatorTest {

    @InjectMocks
    private FinancialAssessmentIdValidator financialAssessmentIdValidator;

    @Mock
    private FinancialAssessmentRepository financialAssessmentRepository;

    @Test
    void testWhenFinancialAssessmentIdIsNull_thenThrowsException() {
        assertThatThrownBy(() -> financialAssessmentIdValidator.validate(null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Financial Assessment id is required");
    }

    @Test
    void testWhenFinancialAssessmentIdIsInvalid_thenThrowsException() {
        assertThatThrownBy(() -> financialAssessmentIdValidator.validate(-1))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Financial Assessment id is required");
    }

    @Test
    void testWhenFinancialAssessmentIdExists_thenValidationPasses() {
        when(financialAssessmentRepository.existsById(anyInt())).thenReturn(true);
        assertThat(financialAssessmentIdValidator.validate(1000)).isNotPresent();
    }

    @Test
    void testWhenFinancialAssessmentIdDoesNotExist_thenThrowsException() {
        when(financialAssessmentRepository.existsById(anyInt())).thenReturn(false);
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> financialAssessmentIdValidator.validate(1000));
        assertThat(validationException.getMessage()).isEqualTo(format("%d is invalid", 1000));
    }
}
