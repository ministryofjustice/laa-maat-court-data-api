package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetailsValidate;
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
public class PreConditionsValidatorTest {

    @Mock
    private MaatIdValidator maatIdValidator;

    @InjectMocks
    private PreConditionsValidator preConditionsValidator;


    @Test
    public void testMaatIdValidator_throwsValidationException() {

        final int testMaatId = 1000;

        when(maatIdValidator.validate(testMaatId))
                .thenThrow(
                        new ValidationException("MAAT id is missing."));
        Assertions.assertThrows(ValidationException.class, ()-> preConditionsValidator.validate(
                CaseDetailsValidate
                        .builder()
                        .maatId(testMaatId)
                        .build()),"MAAT id is missing.");

    }

    @Test
    public void testWhenAllValidatorsExecuted_validationPasses() {

        //given
        final int testMaatId = 1000;

        final CaseDetailsValidate caseDetailsValidate =
                CaseDetailsValidate
                        .builder()
                        .maatId(testMaatId)
                        .build();

        // when
        when(maatIdValidator.validate(testMaatId))
                .thenReturn(Optional.empty());

        preConditionsValidator.validate(caseDetailsValidate);

        //then
        verify(maatIdValidator, times(1)).validate(testMaatId);
    }
}