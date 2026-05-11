package gov.uk.courtdata.reporder.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.RepOrderCCOutcome;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateCCOutcomeValidatorTest {

    @InjectMocks
    private CreateCCOutcomeValidator createCCOutComeValidator;

    @Test
    void givenAUserCreatedIsBlank_whenValidateIsInvoked_thenThrowsException() {
        var outcome = RepOrderCCOutcome.builder().build();
        assertThatThrownBy(() -> createCCOutComeValidator.validate(outcome))
                .isInstanceOf(ValidationException.class)
                .hasMessage("User created is required");
    }

    @Test
    void givenAValidOutcomeParameter_whenValidateIsInvoked_thenReturnSuccess() {
        Optional<Void> result = createCCOutComeValidator.validate(RepOrderCCOutcome.builder()
                .userCreated(TestModelDataBuilder.TEST_USER)
                .build());
        assertThat(result).isNotPresent();
    }
}
