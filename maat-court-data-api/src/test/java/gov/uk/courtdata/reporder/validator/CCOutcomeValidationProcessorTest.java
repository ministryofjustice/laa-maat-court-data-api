package gov.uk.courtdata.reporder.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CCOutcomeValidationProcessorTest {

    private static final Integer INVALID_REP_ID = -1;
    RepOrderCCOutcome repOrderCCOutcome;
    @Mock
    private MaatIdValidator maatIdValidator;
    @Mock
    private CreateCCOutcomeValidator createCCOutComeValidator;
    @InjectMocks
    private CCOutComeValidationProcessor CCOutComeValidationProcessor;

    @BeforeEach
    void setUp() {
        repOrderCCOutcome = TestModelDataBuilder.getRepOrderCCOutcome();
    }

    @Test
    void givenACCOutcomeIdAsNull_whenValidateIsInvoked_thenAllTheValidationIsSuccess() {
        repOrderCCOutcome.setId(null);
        CCOutComeValidationProcessor.validate(repOrderCCOutcome);
        verify(createCCOutComeValidator, atLeastOnce()).validate(any());
        verify(maatIdValidator, atLeastOnce()).validate(any());
    }

    @Test
    void givenACCOutcomeIdAsZero_whenValidateIsInvoked_thenAllTheValidationIsSuccess() {
        repOrderCCOutcome.setId(0);
        CCOutComeValidationProcessor.validate(repOrderCCOutcome);
        verify(createCCOutComeValidator, atLeastOnce()).validate(any());
        verify(maatIdValidator, atLeastOnce()).validate(any());
    }

    @Test
    void givenAEmptyUserCreation_whenValidateIsInvoked_thenThrowValidationException() {
        repOrderCCOutcome.setId(0);
        repOrderCCOutcome.setUserCreated("");
        when(createCCOutComeValidator.validate(any())).thenThrow(new ValidationException());
        assertThatThrownBy(() -> CCOutComeValidationProcessor.validate(repOrderCCOutcome))
                .isInstanceOf(ValidationException.class);
        verify(createCCOutComeValidator, atLeastOnce()).validate(any());
        verify(maatIdValidator, never()).validate(any());
    }

    @Test
    void givenAValidRepId_whenValidateIsInvoked_thenRepIdValidationIsSuccess() {
        CCOutComeValidationProcessor.validate(TestModelDataBuilder.REP_ID);
        verify(maatIdValidator, atLeastOnce()).validate(any());
    }

    @Test
    void givenAInvalidValidRepId_whenValidateIsInvoked_thenReturnValidationException() {
        when(maatIdValidator.validate(any())).thenThrow(new ValidationException());
        assertThatThrownBy(() -> CCOutComeValidationProcessor.validate(INVALID_REP_ID))
                .isInstanceOf(ValidationException.class);
        verify(maatIdValidator, atLeastOnce()).validate(any());
    }

}