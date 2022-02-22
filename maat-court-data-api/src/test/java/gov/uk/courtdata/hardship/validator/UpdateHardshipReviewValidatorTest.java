package gov.uk.courtdata.hardship.validator;

import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.enums.HardshipReviewStatus;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateHardshipReviewValidatorTest {

    @InjectMocks
    private UpdateHardshipReviewValidator updateHardshipReviewValidator;

    @Mock
    private HardshipReviewRepository hardshipReviewRepository;

    @Test
    public void givenStaleTimestamp_whenValidateIsInvoked_thenThrowsException() {
        UpdateHardshipReview mockReview = UpdateHardshipReview.builder()
                .id(1000)
                .updated(LocalDateTime.parse("2022-01-01T15:00:01"))
                .build();
        HardshipReviewEntity mockExisting = HardshipReviewEntity.builder()
                .dateCreated(LocalDateTime.parse("2022-01-01T15:00:00"))
                .status(HardshipReviewStatus.IN_PROGRESS)
                .build();
        when(hardshipReviewRepository.getById(any(Integer.class))).thenReturn(mockExisting);
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> updateHardshipReviewValidator.validate(mockReview));
        assertThat(validationException.getMessage()).isEqualTo("Hardship has been modified by another user");

        mockExisting.setUpdated(LocalDateTime.parse("2022-01-01T15:00:01"));
        assertThat(updateHardshipReviewValidator.validate(mockReview)).isEqualTo(Optional.empty());
    }

    @Test
    public void givenCompletedReview_whenValidateIsInvoked_thenThrowsException() {
        UpdateHardshipReview mockReview = UpdateHardshipReview.builder()
                .id(1000)
                .updated(LocalDateTime.parse("2022-01-01T15:00:00"))
                .build();
        when(hardshipReviewRepository.getById(any(Integer.class))).thenReturn(
                HardshipReviewEntity.builder()
                        .dateCreated(LocalDateTime.parse("2022-01-01T15:00:00"))
                        .status(HardshipReviewStatus.COMPLETE)
                        .build()
        );
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> updateHardshipReviewValidator.validate(mockReview));
        assertThat(validationException.getMessage()).isEqualTo("User cannot modify a complete hardship review");
    }

    @Test
    public void givenCorrectParameters_whenValidateIsInvoked_thenValidationPassed() {
        UpdateHardshipReview mockReview = UpdateHardshipReview.builder()
                .id(1000)
                .updated(LocalDateTime.parse("2022-01-01T15:00:00"))
                .build();
        when(hardshipReviewRepository.getById(any(Integer.class))).thenReturn(
                HardshipReviewEntity.builder()
                        .dateCreated(LocalDateTime.parse("2022-01-01T15:00:00"))
                        .status(HardshipReviewStatus.IN_PROGRESS)
                        .build()
        );
        assertThat(updateHardshipReviewValidator.validate(mockReview)).isEqualTo(Optional.empty());
    }

}
