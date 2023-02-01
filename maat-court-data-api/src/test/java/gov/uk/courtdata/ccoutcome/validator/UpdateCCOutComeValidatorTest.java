package gov.uk.courtdata.ccoutcome.validator;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class UpdateCCOutComeValidatorTest {

    @InjectMocks
    private UpdateCCOutComeValidator updateCCOutComeValidator;

    @Mock
    private CrownCourtProcessingRepository courtProcessingRepository;

    private RepOrderCCOutCome repOrderCCOutCome;

    private static final String BLANK_STRING = "";

    @BeforeEach
    void setUp() {
        repOrderCCOutCome = TestModelDataBuilder.getUpdateRepOrderCCOutCome(123123);
    }

    @Test
    public void givenACCCodeIsBlank_whenValidateIsInvoked_thenThrowsException() {
        repOrderCCOutCome.setCrownCourtCode(BLANK_STRING);
        assertThatThrownBy(() -> updateCCOutComeValidator.validate(repOrderCCOutCome)).isInstanceOf(ValidationException.class)
                .hasMessageContaining(String.format("Crown Court Code is required"));
    }

    @Test
    public void givenACaseNumberIsBlank_whenValidateIsInvoked_thenThrowsException() {
        repOrderCCOutCome.setCaseNumber(BLANK_STRING);
        assertThatThrownBy(() -> updateCCOutComeValidator.validate(repOrderCCOutCome)).isInstanceOf(ValidationException.class)
                .hasMessageContaining(String.format("CaseNumber is required"));
    }

    @Test
    public void givenAUserModifiedIsBlank_whenValidateIsInvoked_thenThrowsException() {
        repOrderCCOutCome.setUserModified(BLANK_STRING);
        assertThatThrownBy(() -> updateCCOutComeValidator.validate(repOrderCCOutCome)).isInstanceOf(ValidationException.class)
                .hasMessageContaining(String.format("User modified is required"));
    }
}