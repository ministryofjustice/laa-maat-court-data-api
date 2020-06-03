package gov.uk.courtdata.link.validator;


import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
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

import static java.lang.String.format;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PreConditionsValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private LinkExistsValidator linkExistsValidator;

    @Mock
    private MaatIdValidator maatIdValidator;

    @Mock
    private CPDataValidator cpDataValidator;

    @InjectMocks
    private PreConditionsValidator preConditionsValidator;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMaatIdValidator_throwsValidationException() {

        final int testMaatId = 1000;

        thrown.expect(ValidationException.class);
        thrown.expectMessage("MAAT id is missing.");

        when(maatIdValidator.validate(testMaatId))
                .thenThrow(
                        new ValidationException("MAAT id is missing."));

        preConditionsValidator.validate(
                CaseDetails
                        .builder()
                        .maatId(testMaatId)
                        .build());

    }


    @Test
    public void testLinkAlreadyExistsValidator_throwsValidationException() {

        final int testMaatId = 1000;


        thrown.expect(ValidationException.class);
        thrown.expectMessage("1000: MaatId already linked to the application.");


        when(linkExistsValidator.validate(testMaatId))
                .thenThrow(new
                        ValidationException(format("%s: MaatId already linked to the application.", testMaatId)));


        preConditionsValidator.validate(
                CaseDetails
                        .builder()
                        .maatId(testMaatId)
                        .build());
    }


    @Test
    public void testCPDataValidator_throwsValidationException() {

        final int testMaatId = 1000;

        CaseDetails request = CaseDetails
                .builder()
                .maatId(testMaatId)
                .build();

        thrown.expect(ValidationException.class);
        thrown.expectMessage("CaseURN can't be null or empty on request.");

        when(cpDataValidator.validate(request))
                .thenThrow(new
                        ValidationException("CaseURN can't be null or empty on request."));

        preConditionsValidator.validate(
                request);
    }


    @Test
    public void testWhenAllValidatorsExecuted_validationPasses() {

        //given
        final int testMaatId = 1000;
        final CaseDetails caseDetails =
                CaseDetails
                        .builder()
                        .maatId(testMaatId)
                        .build();

        // when
        when(maatIdValidator.validate(testMaatId))
                .thenReturn(Optional.empty());
        when(linkExistsValidator.validate(testMaatId))
                .thenReturn(Optional.empty());

        when(cpDataValidator.validate(caseDetails))
                .thenReturn(Optional.empty());


        preConditionsValidator.validate(caseDetails);

        //then
        verify(maatIdValidator, times(1)).validate(testMaatId);
        verify(linkExistsValidator, times(1)).validate(testMaatId);
        verify(cpDataValidator, times(1)).validate(caseDetails);

    }


}
