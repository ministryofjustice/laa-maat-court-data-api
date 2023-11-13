package gov.uk.courtdata.preupdatechecks.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PreUpdateChecksValidationProcessorTest {

    @Mock
    private MaatIdValidator maatIdValidator;

    @InjectMocks
    private PreUpdateChecksValidationProcessor preUpdateChecksValidationProcessor;

    @Test
    void givenAValidRepId_whenValidateIsInvoked_thenRepIdValidationIsSuccess() {
        preUpdateChecksValidationProcessor.validate(REP_ID);
        verify(maatIdValidator, atLeastOnce()).validate(any());
    }

    @Test
    void givenAInvalidValidRepId_whenValidateIsInvoked_thenReturnValidationException() {
        when(maatIdValidator.validate(any())).thenThrow(new ValidationException());
        assertThatThrownBy(() -> preUpdateChecksValidationProcessor.validate(REP_ID))
                .isInstanceOf(ValidationException.class);
        verify(maatIdValidator, atLeastOnce()).validate(any());
    }

}
