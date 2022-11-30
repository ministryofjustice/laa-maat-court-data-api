package gov.uk.courtdata.link.validator;


import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
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

    @Mock
    private CPDataValidator cpDataValidator;

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
    public void testCPDataValidator_throwsValidationException() {

        final int testMaatId = 1000;

        CaseDetailsValidate request = CaseDetailsValidate
                .builder()
                .maatId(testMaatId)
                .build();

        when(cpDataValidator.validate(CaseDetails
                .builder()
                .maatId(testMaatId)
                .build()))
                .thenThrow(new
                        ValidationException("CaseURN can't be null or empty on request."));
        Assertions.assertThrows(ValidationException.class, ()-> preConditionsValidator.validate(
                request),"CaseURN can't be null or empty on request.");
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

        final CaseDetails caseDetails = CaseDetails
                .builder()
                .maatId(testMaatId)
                .build();

        // when
        when(maatIdValidator.validate(testMaatId))
                .thenReturn(Optional.empty());

        when(cpDataValidator.validate(CaseDetails
                .builder()
                .maatId(testMaatId)
                .build()))
                .thenReturn(Optional.empty());


        preConditionsValidator.validate(caseDetailsValidate);

        //then
        verify(maatIdValidator, times(1)).validate(testMaatId);
        verify(cpDataValidator, times(1)).validate(caseDetails);

    }


}
