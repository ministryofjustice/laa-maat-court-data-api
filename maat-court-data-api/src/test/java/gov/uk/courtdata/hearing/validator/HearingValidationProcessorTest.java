package gov.uk.courtdata.hearing.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.validator.LinkRegisterValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HearingValidationProcessorTest {

    @Mock
    private MaatIdValidator maatIdValidator;
    @Mock
    private LinkRegisterValidator linkRegisterValidator;
    @InjectMocks
    private HearingValidationProcessor hearingValidationProcessor;


    @Test
    public void testWhenAnyValidatorFails_throwsValidationException() {

        final int testMaatId = 1000;

        when(maatIdValidator.validate(testMaatId))
                .thenThrow(
                        new ValidationException("MAAT id is missing."));
        Assertions.assertThrows(ValidationException.class,()->{
            hearingValidationProcessor.validate(HearingResulted.builder().maatId(testMaatId).build());},
                "MAAT id is missing." );


    }

    @Test
    public void testWhenAllValidatorsPass_validationPasses() {

        //given
        final int testMaatId = 1000;
        final HearingResulted hearingDetails = HearingResulted.builder().maatId(testMaatId).build();

        // when
        when(maatIdValidator.validate(testMaatId))
                .thenReturn(Optional.empty());

        hearingValidationProcessor.validate(hearingDetails);

        //then
        verify(maatIdValidator, times(1)).validate(testMaatId);
        verify(linkRegisterValidator, times(1)).validate(testMaatId);
    }
}
