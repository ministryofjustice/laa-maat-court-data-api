package gov.uk.courtdata.ccoutcome.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class CreateCCOutComeValidatorTest {

    @InjectMocks
    private CreateCCOutComeValidator createCCOutComeValidator;

    @Test
    public void givenAUserCreatedIsBlank_whenValidateIsInvoked_thenThrowsException() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> createCCOutComeValidator.validate(RepOrderCCOutCome.builder().build()));
        assertThat(validationException.getMessage()).isEqualTo("User created is required");
    }
}