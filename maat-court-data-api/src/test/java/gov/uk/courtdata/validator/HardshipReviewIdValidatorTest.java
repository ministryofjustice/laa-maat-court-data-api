package gov.uk.courtdata.validator;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.hardship.validator.HardshipReviewIdValidator;
import gov.uk.courtdata.repository.HardshipReviewRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HardshipReviewIdValidatorTest {

    @InjectMocks
    private HardshipReviewIdValidator hardshipReviewIdValidator;

    @Mock
    private HardshipReviewRepository hardshipReviewRepository;

    @Test
    void testWhenFinancialAssessmentIdIsNull_thenThrowsException() {
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> hardshipReviewIdValidator.validate(null));
        assertThat(validationException.getMessage()).isEqualTo("Hardship review id is required");
    }

    @Test
    void testWhenFinancialAssessmentIdIsInvalid_thenThrowsException() {
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> hardshipReviewIdValidator.validate(-1));
        assertThat(validationException.getMessage()).isEqualTo("Hardship review id is required");
    }

    @Test
    void testWhenFinancialAssessmentIdExists_thenValidationPasses() {
        when(hardshipReviewRepository.existsById(any())).thenReturn(true);
        assertThat(hardshipReviewIdValidator.validate(1000)).isNotPresent();
    }

    @Test
    void testWhenFinancialAssessmentIdDoesNotExist_thenThrowsException() {
        when(hardshipReviewRepository.existsById(any())).thenReturn(false);
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> hardshipReviewIdValidator.validate(1000));
        assertThat(validationException.getMessage()).isEqualTo(format("%d is invalid", 1000));
    }
}
