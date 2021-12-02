package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FinancialAssessmentIdValidatorTest {

    @InjectMocks
    private FinancialAssessmentIdValidator financialAssessmentIdValidator;

    @Mock
    private FinancialAssessmentRepository financialAssessmentRepository;

    @Test
    public void testWhenFinancialAssessmentIdIsNull_thenThrowsException() {
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> financialAssessmentIdValidator.validate(null));
        assertThat(validationException.getMessage()).isEqualTo("Financial Assessment id is required");
    }

    @Test
    public void testWhenFinancialAssessmentIdIsInvalid_thenThrowsException() {
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> financialAssessmentIdValidator.validate(-1));
        assertThat(validationException.getMessage()).isEqualTo("Financial Assessment id is required");
    }

    @Test
    public void testWhenFinancialAssessmentIdExists_thenValidationPasses() {
        when(financialAssessmentRepository.findById(any())).thenReturn(java.util.Optional.of(new FinancialAssessmentEntity()));
        assertThat(financialAssessmentIdValidator.validate(1000)).isEqualTo(Optional.empty());
    }

    @Test
    public void testWhenFinancialAssessmentIdDoesNotExist_thenThrowsException() {
        when(financialAssessmentRepository.findById(any())).thenReturn(Optional.empty());
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> financialAssessmentIdValidator.validate(1000));
        assertThat(validationException.getMessage()).isEqualTo(format("%d is invalid", 1000));
    }
}
