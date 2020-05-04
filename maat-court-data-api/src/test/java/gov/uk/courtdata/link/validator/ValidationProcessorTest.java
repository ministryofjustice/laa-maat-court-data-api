package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.validator.DefendantValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import gov.uk.courtdata.validator.ReferenceDataValidator;
import gov.uk.courtdata.validator.SolicitorValidator;
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
public class ValidationProcessorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private MaatIdValidator maatIdValidator;
    @Mock
    private LinkExistsValidator linkExistsValidator;
    @Mock
    private DefendantValidator defendantValidator;
    @Mock
    private SolicitorValidator solicitorValidator;
    @Mock
    private CourtValidator courtValidator;
    @Mock
    private ReferenceDataValidator referenceDataValidator;
    @Mock
    private CPDataValidator CPDataValidator;

    @InjectMocks
    private ValidationProcessor validationProcessor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWhenAnyValidatorFails_throwsValidationException() {

        final int testMaatId = 1000;

        thrown.expect(ValidationException.class);
        thrown.expectMessage("MAAT id is missing.");
        when(maatIdValidator.validate(testMaatId))
                .thenThrow(
                        new ValidationException("MAAT id is missing."));

        validationProcessor.validate(CaseDetails.builder().maatId(testMaatId).build());

    }


    @Test
    public void testWhenAllValidatorsSuccess_validationPasses() {

        //given
        final int testMaatId = 1000;
        final CaseDetails caseDetails = CaseDetails.builder().maatId(testMaatId).build();

        // when
        when(maatIdValidator.validate(testMaatId))
                .thenReturn(Optional.empty());
        when(linkExistsValidator.validate(testMaatId))
                .thenReturn(
                        Optional.empty());
        when(defendantValidator.validate(testMaatId))
                .thenReturn(
                        Optional.of(DefendantMAATDataEntity.builder().maatId(testMaatId).build()));

        when(solicitorValidator.validate(testMaatId))
                .thenReturn(
                        Optional.of(SolicitorMAATDataEntity.builder().maatId(testMaatId).build()));
        when(courtValidator.validate(caseDetails))
                .thenReturn(
                        Optional.empty());
        when(referenceDataValidator.validate(caseDetails))
                .thenReturn(
                        Optional.empty());
        when(CPDataValidator.validate(caseDetails))
                .thenReturn(Optional.empty());


        validationProcessor.validate(caseDetails);

        //then
        verify(maatIdValidator, times(1)).validate(testMaatId);
        verify(linkExistsValidator, times(1)).validate(testMaatId);
        verify(defendantValidator, times(1)).validate(testMaatId);
        verify(solicitorValidator, times(1)).validate(testMaatId);
        verify(courtValidator, times(1)).validate(caseDetails);
        verify(referenceDataValidator, times(1)).validate(caseDetails);
        verify(CPDataValidator, times(1)).validate(caseDetails);

    }

}
