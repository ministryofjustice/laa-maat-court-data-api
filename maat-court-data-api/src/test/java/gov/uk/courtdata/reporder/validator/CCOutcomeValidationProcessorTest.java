package gov.uk.courtdata.reporder.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.RepOrderCCOutcome;
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
class CCOutcomeValidationProcessorTest {

    private static final Integer INVALID_REP_ID = -1;
    @Mock
    private MaatIdValidator maatIdValidator;
    @Mock
    private CreateCCOutcomeValidator createCCOutComeValidator;
    @InjectMocks
    private CCOutComeValidationProcessor CCOutComeValidationProcessor;

    @Test
    void givenACCOutcomeIdAsNull_whenValidateIsInvoked_thenAllTheValidationIsSuccess() {
        RepOrderCCOutcome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutCome();
        repOrderCCOutCome.setId(null);
        CCOutComeValidationProcessor.validate(repOrderCCOutCome);
    }

    @Test
    void givenACCOutcomeIdAsZero_whenValidateIsInvoked_thenAllTheValidationIsSuccess() {
        RepOrderCCOutcome repOrderCCOutCome = TestModelDataBuilder.getRepOrderCCOutCome();
        repOrderCCOutCome.setId(0);
        CCOutComeValidationProcessor.validate(repOrderCCOutCome);
    }

    @Test
    void givenAValidRepId_whenValidateIsInvoked_thenRepIdValidationIsSuccess() {
        when(maatIdValidator.validate(any())).thenReturn(Optional.empty());
        CCOutComeValidationProcessor.validate(TestModelDataBuilder.REP_ID);
    }

    @Test
    void givenAInvalidValidRepId_whenValidateIsInvoked_thenReturnValidationException() {
        when(maatIdValidator.validate(any())).thenThrow(new ValidationException());
        assertThatThrownBy(() -> CCOutComeValidationProcessor.validate(INVALID_REP_ID)).isInstanceOf(ValidationException.class);
    }

}