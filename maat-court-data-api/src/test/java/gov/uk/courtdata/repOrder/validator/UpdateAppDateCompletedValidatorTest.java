package gov.uk.courtdata.repOrder.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UpdateAppDateCompletedValidatorTest {

    @InjectMocks
    private UpdateAppDateCompletedValidator updateAppDateCompletedValidator;

    @Mock
    private MaatIdValidator maatIdValidator;

    @Test
    public void givenValidParameters_whenValidateIsInvoked_thenValidationPasses() {
        Optional<Void> result = updateAppDateCompletedValidator
                .validate(TestModelDataBuilder.getUpdateAppDateCompleted());
        verify(maatIdValidator).validate(anyInt());
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void givenMissingRepId_whenValidateIsInvoked_thenValidationExceptionIsThrown() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> updateAppDateCompletedValidator.validate(
                        UpdateAppDateCompleted.builder().assessmentDateCompleted(LocalDateTime.now()).build()
                )
        );
        assertThat(validationException.getMessage())
                .isEqualTo("Rep Id is missing from request and is required");
    }

    @Test
    public void givenMissingAssessmentDate_whenValidateIsInvoked_thenValidationExceptionIsThrown() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> updateAppDateCompletedValidator.validate(
                        UpdateAppDateCompleted.builder().repId(TestModelDataBuilder.REP_ID).build()
                )
        );
        assertThat(validationException.getMessage())
                .isEqualTo("Assessment Date completed is missing from request and is required");
    }
}