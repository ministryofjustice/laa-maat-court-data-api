package gov.uk.courtdata.ccoutcome.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CCOutComeValidatorTest {

    @Mock
    private MaatIdValidator maatIdValidator;
    @Mock
    private CreateCCOutComeValidator createCCOutComeValidator;

    @Mock
    private UpdateCCOutComeValidator updateCCOutComeValidator;
    @InjectMocks
    private CCOutComeValidator CCOutComeValidator;

    @Test
    public void givenAValidCreatedCCOutComeParameter_whenValidateIsInvoked_shouldSuccess() {
        RepOrderCCOutCome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutCome();
        repOrderCCOutCome.setId(null);
        when(maatIdValidator.validate(any())).thenReturn(Optional.empty());
        when(createCCOutComeValidator.validate(any())).thenReturn(Optional.empty());
        CCOutComeValidator.validate(repOrderCCOutCome);
    }

    @Test
    public void givenAInValidCreatedCCOutComeParameter_whenValidateIsInvoked_shouldReturnValidationException() {
        RepOrderCCOutCome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutCome();
        repOrderCCOutCome.setId(null);
        when(maatIdValidator.validate(any())).thenThrow(new ValidationException());
        assertThatThrownBy(() -> CCOutComeValidator.validate(TestModelDataBuilder.getRepOrderCCOutCome()))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    public void givenAValidUpdatedCCOutComeParameter_whenValidateIsInvoked_shouldSuccess() {
        when(maatIdValidator.validate(any())).thenReturn(Optional.empty());
        CCOutComeValidator.validate(TestModelDataBuilder.getRepOrderCCOutCome());
    }

    @Test
    public void givenAValid_UpdatedCCOutComeParameter_whenValidateIsInvoked_shouldSuccess() {
        when(maatIdValidator.validate(any())).thenReturn(Optional.empty());
        CCOutComeValidator.validate(TestModelDataBuilder.getRepOrderCCOutCome());
    }

    @Test
    public void givenAValidRepId_whenValidateIsInvoked_shouldSuccess() {
        when(maatIdValidator.validate(any())).thenReturn(Optional.empty());
        CCOutComeValidator.validate(TestModelDataBuilder.REP_ID);
    }

    @Test
    public void givenAInvalidValidRepId_whenValidateIsInvoked_shouldSuccess() {
        when(maatIdValidator.validate(any())).thenThrow(new ValidationException());
        assertThatThrownBy(() -> CCOutComeValidator.validate(-1)).isInstanceOf(ValidationException.class);
    }

}