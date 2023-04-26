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

import static java.lang.String.format;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PreConditionsValidatorTest {

    private static final Integer TEST_MAAT_ID = 1000;
    @Mock
    private MaatIdValidator maatIdValidator;
    @Mock
    private CPDataValidator cpDataValidator;
    @Mock
    private LinkExistsValidator linkExistsValidator;
    @InjectMocks
    private PreConditionsValidator preConditionsValidator;

    @Test
    public void testMaatIdValidator_throwsValidationException() {
        final CaseDetailsValidate request = getTestCaseDetailsValidate();
        final String expectedErrorMessage = "MAAT id is missing.";

        when(maatIdValidator.validate(TEST_MAAT_ID)).thenThrow(new ValidationException(expectedErrorMessage));

        Assertions.assertThrows(ValidationException.class, () -> preConditionsValidator.validate(request), expectedErrorMessage);
    }


    @Test
    public void testCPDataValidator_throwsValidationException() {

        final CaseDetailsValidate request = getTestCaseDetailsValidate();
        final CaseDetails caseDetails = getTestCaseDetails();
        final String expectedErrorMessage = "CaseURN can't be null or empty on request.";

        when(cpDataValidator.validate(caseDetails)).thenThrow(new ValidationException(expectedErrorMessage));

        Assertions.assertThrows(ValidationException.class, () -> preConditionsValidator.validate(request), expectedErrorMessage);
    }

    @Test
    public void testLinkExistsValidator_throwsValidationException() {
        final CaseDetailsValidate request = getTestCaseDetailsValidate();
        final String expectedErrorMessage = format("%s is already linked to a case.", TEST_MAAT_ID);

        when(linkExistsValidator.validate(TEST_MAAT_ID)).thenThrow(new ValidationException(expectedErrorMessage));

        Assertions.assertThrows(ValidationException.class, () -> preConditionsValidator.validate(request), expectedErrorMessage);
    }


    @Test
    public void testWhenAllValidatorsExecuted_validationPasses() {

        //given
        final CaseDetailsValidate caseDetailsValidate = getTestCaseDetailsValidate();
        final CaseDetails caseDetails = getTestCaseDetails();

        // when
        when(maatIdValidator.validate(TEST_MAAT_ID)).thenReturn(Optional.empty());

        when(linkExistsValidator.validate(TEST_MAAT_ID)).thenReturn(Optional.empty());

        when(cpDataValidator.validate(caseDetails)).thenReturn(Optional.empty());

        preConditionsValidator.validate(caseDetailsValidate);

        //then
        verify(maatIdValidator, times(1)).validate(TEST_MAAT_ID);
        verify(linkExistsValidator, times(1)).validate(TEST_MAAT_ID);
        verify(cpDataValidator, times(1)).validate(caseDetails);

    }

    private CaseDetails getTestCaseDetails() {
        return CaseDetails.builder().maatId(TEST_MAAT_ID).build();
    }

    private CaseDetailsValidate getTestCaseDetailsValidate() {
        return CaseDetailsValidate.builder().maatId(TEST_MAAT_ID).build();
    }


}
