package gov.uk.courtdata.hardship.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReview;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HardshipReviewValidationProcessorTest {

    @InjectMocks
    private HardshipReviewValidationProcessor hardshipReviewValidationProcessor;

    @Mock
    private HardshipReviewIdValidator hardshipReviewIdValidator;

    @Mock
    private CreateHardshipReviewValidator createHardshipReviewValidator;

    @Mock
    private UpdateHardshipReviewValidator updateHardshipReviewValidator;

    @Test
    public void givenHardshipReviewId_whenValidateIsInvoked_thenIdValidatorIsCalled() {
        hardshipReviewValidationProcessor.validate(1000);
        verify(hardshipReviewIdValidator).validate(any(Integer.class));
    }

    @Test
    public void givenNullHardshipReview_whenValidateIsInvoked_thenThrowsException() {
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> hardshipReviewValidationProcessor.validate((HardshipReview) null));
        assertThat(validationException.getMessage()).isEqualTo("Hardship review Request is empty");
    }

    @Test
    public void givenCreateHardshipReview_whenValidateIsInvoked_thenCreateValidatorIsCalled() {
        hardshipReviewValidationProcessor.validate(CreateHardshipReview.builder().build());
        verify(createHardshipReviewValidator).validate((any(CreateHardshipReview.class)));
    }

    @Test
    public void givenUpdateHardshipReview_whenValidateIsInvoked_thenUpdateValidatorIsCalled() {
        hardshipReviewValidationProcessor.validate(UpdateHardshipReview.builder().build());
        verify(updateHardshipReviewValidator).validate((any(UpdateHardshipReview.class)));
    }

}
