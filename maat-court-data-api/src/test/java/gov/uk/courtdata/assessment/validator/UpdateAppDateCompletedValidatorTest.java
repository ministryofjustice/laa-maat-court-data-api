package gov.uk.courtdata.assessment.validator;

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


@ExtendWith(MockitoExtension.class)
class UpdateAppDateCompletedValidatorTest {

    @InjectMocks
    private UpdateAppDateCompletedValidator updateAppDateCompletedValidator;

    @Mock
    private MaatIdValidator maatIdValidator;


    @Test
    public void testUpdateAppDateCompletedValidator_whenRepIdIDIsDefine_thenValidationPasses() {
        Optional<Void> result = updateAppDateCompletedValidator.validate(getUpdateAppDateCompletedWithRepId(1000));
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void testUpdateAppDateCompletedValidator_whenRepIdIDIsNull_thenValidationPasses() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> updateAppDateCompletedValidator.validate(getUpdateAppDateCompletedWithRepId(null)));
        assertThat(validationException.getMessage()).isEqualTo("Rep Id is missing from request and is required");
    }

    @Test
    public void testUpdateAppDateCompletedValidator_whenAppDateCompletedIsDefine_thenValidationPasses() {
        Optional<Void> result = updateAppDateCompletedValidator.validate(getUpdateAppDateCompleted(LocalDateTime.now()));
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    public void testUpdateAppDateCompletedValidator_whenAppDateCompletedIsNull_thenThrowValidationException() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> updateAppDateCompletedValidator.validate(getUpdateAppDateCompleted(null)));
        assertThat(validationException.getMessage()).isEqualTo("Assessment Date completed is missing from request and is required");
    }


    private UpdateAppDateCompleted getUpdateAppDateCompletedWithRepId(Integer repId) {

        UpdateAppDateCompleted updateAppDateCompleted = new UpdateAppDateCompleted();
        updateAppDateCompleted.setRepId(repId);
        updateAppDateCompleted.setAssessmentDateCompleted(LocalDateTime.now());

        return updateAppDateCompleted;
    }


    private UpdateAppDateCompleted getUpdateAppDateCompleted(LocalDateTime appDateCompleted) {

        UpdateAppDateCompleted updateAppDateCompleted = new UpdateAppDateCompleted();
        updateAppDateCompleted.setAssessmentDateCompleted(appDateCompleted);
        updateAppDateCompleted.setRepId(TestModelDataBuilder.REP_ORDERS_ID);
        return updateAppDateCompleted;
    }

}