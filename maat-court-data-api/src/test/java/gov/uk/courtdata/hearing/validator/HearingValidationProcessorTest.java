package gov.uk.courtdata.hearing.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hearing.HearingDetails;
import gov.uk.courtdata.validator.LinkRegisterValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HearingValidationProcessorTest {

    @Mock
    private MaatIdValidator maatIdValidator;
    @Mock
    private LinkRegisterValidator linkRegisterValidator;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @InjectMocks
    private HearingValidationProcessor hearingValidationProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testWhenAnyValidatorFails_throwsValidationException() {

        final int testMaatId = 1000;

        exception.expect(ValidationException.class);
        exception.expectMessage("MAAT id is missing.");
        when(maatIdValidator.validate(testMaatId))
                .thenThrow(
                        new ValidationException("MAAT id is missing."));

        hearingValidationProcessor.validate(HearingDetails.builder().maatId(testMaatId).build());

    }

    @Test
    public void testWhenAllValidatorsPass_validationPasses() {

        //given
        final int testMaatId = 1000;
        final HearingDetails hearingDetails = HearingDetails.builder().maatId(testMaatId).build();

        // when
        when(maatIdValidator.validate(testMaatId))
                .thenReturn(Optional.empty());

        hearingValidationProcessor.validate(hearingDetails);

        //then
        verify(maatIdValidator, times(1)).validate(testMaatId);
        verify(linkRegisterValidator, times(1)).validate(testMaatId);
    }
}
