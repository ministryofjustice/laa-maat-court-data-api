package gov.uk.courtdata.hardship.validator;

import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
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
public class CreateHardshipReviewValidatorTest {

    @InjectMocks
    private CreateHardshipReviewValidator createHardshipReviewValidator;

    @Mock
    private FinancialAssessmentRepository financialAssessmentRepository;

    @Test
    public void givenIncompleteAssessment_whenValidateIsInvoked_thenThrowsException() {
        when(financialAssessmentRepository.findCompletedAssessmentByRepId(any(Integer.class))).thenReturn(
                Optional.empty()
        );
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> createHardshipReviewValidator.validate(CreateHardshipReview.builder().build()));
        assertThat(validationException.getMessage()).isEqualTo("Review can only be entered after a completed assessment");
    }

    @Test
    public void givenStaleReviewDate_whenValidateIsInvoked_thenThrowsException() {
        CreateHardshipReview mockHardship = CreateHardshipReview.builder()
                .repId(1000)
                .reviewDate(LocalDateTime.parse("2021-01-01T15:00:00"))
                .build();
        FinancialAssessmentEntity mockAssessment = FinancialAssessmentEntity.builder()
                .initialAssessmentDate(LocalDateTime.parse("2022-01-01T15:00:00"))
                .build();

        when(financialAssessmentRepository.findCompletedAssessmentByRepId(any(Integer.class))).thenReturn(
                Optional.of(mockAssessment)
        );
        ValidationException validationException = Assert.assertThrows(ValidationException.class,
                () -> createHardshipReviewValidator.validate(mockHardship));
        assertThat(validationException.getMessage()).isEqualTo("Review date cannot pre-date the means assessment date");

        mockAssessment.setFullAssessmentDate(LocalDateTime.parse("2022-02-01T15:00:00"));

        validationException = Assert.assertThrows(ValidationException.class,
                () -> createHardshipReviewValidator.validate(mockHardship));
        assertThat(validationException.getMessage()).isEqualTo("Review date cannot pre-date the means assessment date");
    }

    @Test
    public void givenCorrectParameters_whenValidateIsInvoked_thenValidationPasses() {
        CreateHardshipReview mockHardship = CreateHardshipReview.builder()
                .repId(1000)
                .reviewDate(LocalDateTime.parse("2023-01-01T15:00:00"))
                .build();
        FinancialAssessmentEntity mockAssessment = FinancialAssessmentEntity.builder()
                .initialAssessmentDate(LocalDateTime.parse("2022-01-01T15:00:00"))
                .build();
        when(financialAssessmentRepository.findCompletedAssessmentByRepId(any(Integer.class))).thenReturn(
                Optional.of(mockAssessment)
        );
        assertThat(createHardshipReviewValidator.validate(mockHardship)).isEqualTo(Optional.empty());
    }

}
