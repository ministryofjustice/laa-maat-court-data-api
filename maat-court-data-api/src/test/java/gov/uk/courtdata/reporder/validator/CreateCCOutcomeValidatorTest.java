package gov.uk.courtdata.reporder.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CreateCCOutcomeValidatorTest {

    @InjectMocks
    private CreateCCOutcomeValidator createCCOutComeValidator;

    @Test
    void givenAUserCreatedIsBlank_whenValidateIsInvoked_thenThrowsException() {
        assertThatThrownBy(() -> createCCOutComeValidator.validate(RepOrderCCOutcome.builder().build()))
                .isInstanceOf(ValidationException.class).hasMessage("User created is required");
    }

    @Test
    void givenAValidOutcomeParameter_whenValidateIsInvoked_thenReturnSuccess() {
        Optional<Void> result = createCCOutComeValidator.validate(RepOrderCCOutcome.builder()
                .userCreated(TestModelDataBuilder.TEST_USER).build());
        assertThat(result).isEqualTo(Optional.empty());
    }
}