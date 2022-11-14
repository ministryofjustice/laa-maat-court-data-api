package gov.uk.courtdata.reporder.validator;


import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.reporder.validator.UpdateRepOrderValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateRepOrderValidatorTest {

    @InjectMocks
    private UpdateRepOrderValidator updateRepOrderValidator;

    @Mock
    private MaatIdValidator maatIdValidator;

    @Test
    public void givenMissingRepId_whenValidateIsInvoked_thenValidationExceptionIsThrown() {
        ValidationException validationException = Assertions.assertThrows(ValidationException.class,
                () -> updateRepOrderValidator.validate(UpdateRepOrder.builder().build())
        );
        assertThat(validationException.getMessage())
                .isEqualTo("Rep Id is missing from request and is required");
    }

    @Test
    public void givenValidRepId_whenValidateIsInvoked_thenValidationPasses() {
        Optional<Void> result = updateRepOrderValidator
                .validate(TestModelDataBuilder.getUpdateRepOrder());
        verify(maatIdValidator).validate(anyInt());
        assertThat(result).isEqualTo(Optional.empty());
    }

}