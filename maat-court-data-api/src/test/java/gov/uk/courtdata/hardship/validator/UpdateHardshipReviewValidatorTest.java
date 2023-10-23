package gov.uk.courtdata.hardship.validator;

import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.enums.HardshipReviewStatus;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateHardshipReviewValidatorTest {

    @InjectMocks
    private UpdateHardshipReviewValidator updateHardshipReviewValidator;

    @Mock
    private HardshipReviewRepository hardshipReviewRepository;

    @Test
    void givenCompletedReview_whenValidateIsInvoked_thenThrowsException() {
        UpdateHardshipReview mockReview = UpdateHardshipReview.builder()
                .id(1000)
                .updated(LocalDateTime.parse("2022-01-01T15:00:00"))
                .build();
        when(hardshipReviewRepository.getReferenceById(any(Integer.class))).thenReturn(
                HardshipReviewEntity.builder()
                        .dateCreated(LocalDateTime.parse("2022-01-01T15:00:00"))
                        .status(HardshipReviewStatus.COMPLETE)
                        .build()
        );
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> updateHardshipReviewValidator.validate(mockReview));
        assertThat(validationException.getMessage()).isEqualTo("User cannot modify a complete hardship review");
    }

    @Test
    void givenCorrectParameters_whenValidateIsInvoked_thenValidationPassed() {
        UpdateHardshipReview mockReview = UpdateHardshipReview.builder()
                .id(1000)
                .updated(LocalDateTime.parse("2022-01-01T15:00:00"))
                .build();
        when(hardshipReviewRepository.getReferenceById(any(Integer.class))).thenReturn(
                HardshipReviewEntity.builder()
                        .dateCreated(LocalDateTime.parse("2022-01-01T15:00:00"))
                        .status(HardshipReviewStatus.IN_PROGRESS)
                        .build()
        );
        assertThat(updateHardshipReviewValidator.validate(mockReview)).isNotPresent();
    }

}
