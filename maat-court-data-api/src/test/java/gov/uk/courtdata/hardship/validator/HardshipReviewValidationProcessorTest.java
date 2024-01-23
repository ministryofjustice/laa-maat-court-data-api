package gov.uk.courtdata.hardship.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class HardshipReviewValidationProcessorTest {

    @InjectMocks
    private HardshipReviewValidationProcessor hardshipReviewValidationProcessor;

    @Mock
    private HardshipReviewIdValidator hardshipReviewIdValidator;

    @Test
    public void givenHardshipReviewId_whenValidateIsInvoked_thenIdValidatorIsCalled() {
        hardshipReviewValidationProcessor.validate(1000);
        verify(hardshipReviewIdValidator).validate(any(Integer.class));
    }
}
