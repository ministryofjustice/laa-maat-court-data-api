package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.hardship.validator.HardshipReviewIdValidator;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HardshipReviewIdValidatorTest {

    @InjectMocks
    private HardshipReviewIdValidator hardshipReviewIdValidator;

    @Mock
    private HardshipReviewRepository hardshipReviewRepository;

    @Test
    public void testWhenFinancialAssessmentIdIsNull_thenThrowsException() {
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> hardshipReviewIdValidator.validate(null));
        assertThat(validationException.getMessage()).isEqualTo("Hardship review id is required");
    }

    @Test
    public void testWhenFinancialAssessmentIdIsInvalid_thenThrowsException() {
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> hardshipReviewIdValidator.validate(-1));
        assertThat(validationException.getMessage()).isEqualTo("Hardship review id is required");
    }

    @Test
    public void testWhenFinancialAssessmentIdExists_thenValidationPasses() {
        when(hardshipReviewRepository.existsById(any())).thenReturn(true);
        assertThat(hardshipReviewIdValidator.validate(1000)).isEqualTo(Optional.empty());
    }

    @Test
    public void testWhenFinancialAssessmentIdDoesNotExist_thenThrowsException() {
        when(hardshipReviewRepository.existsById(any())).thenReturn(false);
        ValidationException validationException = assertThrows(ValidationException.class,
                () -> hardshipReviewIdValidator.validate(1000));
        assertThat(validationException.getMessage()).isEqualTo(format("%d is invalid", 1000));
    }
}
