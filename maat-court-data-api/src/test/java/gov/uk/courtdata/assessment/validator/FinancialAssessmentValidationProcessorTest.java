package gov.uk.courtdata.assessment.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreateFinancialAssessment;
import gov.uk.courtdata.model.assessment.FinancialAssessment;
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
public class FinancialAssessmentValidationProcessorTest {

    @InjectMocks
    private FinancialAssessmentValidationProcessor financialAssessmentValidationProcessor;

    @Mock
    private CreateAssessmentValidator createAssessmentValidator;

    @Mock
    private FinancialAssessmentIdValidator financialAssessmentIdValidator;

    @Test
    public void testFinancialAssessmentValidationProcessor_whenIdIsPassed_thenCallsIdValidator() {
        when(financialAssessmentIdValidator.validate(any(Integer.class))).thenReturn(Optional.empty());
        financialAssessmentValidationProcessor.validate(1000);
        verify(financialAssessmentIdValidator, times(1)).validate(1000);
    }

    @Test
    public void testFinancialAssessmentValidationProcessor_whenAssessmentIsNull_thenThrowsException() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> financialAssessmentValidationProcessor.validate((FinancialAssessment) null));
        assertThat(validationException.getMessage()).isEqualTo("Financial Assessment Request is empty");
    }

    @Test
    public void testFinancialAssessmentValidationProcessor_whenRepIdIsNull_thenThrowsException() {
        FinancialAssessment assessment = TestModelDataBuilder.getCreateFinancialAssessment();
        assessment.setRepId(null);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> financialAssessmentValidationProcessor.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Rep Order ID is required");
    }

    @Test
    public void testFinancialAssessmentValidationProcessor_whenInitialAscrIdIsNull_thenThrowsException() {
        FinancialAssessment assessment = TestModelDataBuilder.getCreateFinancialAssessment();
        assessment.setInitialAscrId(null);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> financialAssessmentValidationProcessor.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Assessment Criteria ID is required");
    }

    @Test
    public void testFinancialAssessmentValidationProcessor_whenCmuIdIsNull_thenThrowsException() {
        FinancialAssessment assessment = TestModelDataBuilder.getCreateFinancialAssessment();
        assessment.setCmuId(null);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> financialAssessmentValidationProcessor.validate(assessment));
        assertThat(validationException.getMessage()).isEqualTo("Case management unit ID is required");
    }

    @Test
    public void testFinancialAssessmentValidationProcessor_whenRequiredFieldsPresent_thenValidationPasses() {
        FinancialAssessment assessment = TestModelDataBuilder.getCreateFinancialAssessment();
        when(createAssessmentValidator.validate(any(CreateFinancialAssessment.class))).thenReturn(Optional.empty());
        assertThat(financialAssessmentValidationProcessor.validate(assessment)).isEqualTo(Optional.empty());
    }
}
