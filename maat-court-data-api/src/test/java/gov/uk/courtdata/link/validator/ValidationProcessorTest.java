package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.validator.DefendantValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import gov.uk.courtdata.validator.ReferenceDataValidator;
import gov.uk.courtdata.validator.SolicitorValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidationProcessorTest {

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

    @Test
    public void testWhenAnyValidatorFails_throwsValidationException() {

        final int testMaatId = 1000;

        when(maatIdValidator.validate(testMaatId))
                .thenThrow(
                        new ValidationException("MAAT id is missing."));
        Assertions.assertThrows(ValidationException.class, ()->
                validationProcessor.validate(CaseDetails.builder().maatId(testMaatId).build()),"MAAT id is missing.");

    }


    @Test
    public void testWhenAllValidatorsSuccess_validationPasses() {

        //given
        final int testMaatId = 1000;
        final CaseDetails caseDetails = CaseDetails.builder().maatId(testMaatId).build();
        final DefendantMAATDataEntity defendantMAATDataEntity = DefendantMAATDataEntity.builder().maatId(testMaatId).build();
        final SolicitorMAATDataEntity solicitorMAATDataEntity = SolicitorMAATDataEntity.builder().maatId(testMaatId).build();
        // when
        when(maatIdValidator.validate(testMaatId))
                .thenReturn(Optional.empty());
        when(linkExistsValidator.validate(testMaatId))
                .thenReturn(
                        Optional.empty());

        when(defendantValidator.validate(testMaatId))
                .thenReturn(
                        Optional.of(defendantMAATDataEntity));

        when(solicitorValidator.validate(testMaatId))
                .thenReturn(
                        Optional.of(solicitorMAATDataEntity));
        when(courtValidator.validate(caseDetails))
                .thenReturn(
                        Optional.empty());
        when(referenceDataValidator.validate(caseDetails))
                .thenReturn(
                        Optional.empty());
        when(CPDataValidator.validate(caseDetails))
                .thenReturn(Optional.empty());


        CourtDataDTO response = validationProcessor.validate(caseDetails);

        //then
        verify(maatIdValidator, times(1)).validate(testMaatId);
        verify(linkExistsValidator, times(1)).validate(testMaatId);
        verify(defendantValidator, times(1)).validate(testMaatId);
        verify(solicitorValidator, times(1)).validate(testMaatId);
        verify(courtValidator, times(1)).validate(caseDetails);
        verify(referenceDataValidator, times(1)).validate(caseDetails);
        verify(CPDataValidator, times(1)).validate(caseDetails);

        CourtDataDTO actual = CourtDataDTO.builder()
                .caseDetails(caseDetails)
                .solicitorMAATDataEntity(solicitorMAATDataEntity)
                .defendantMAATDataEntity(defendantMAATDataEntity)
                .build();
        assertThat(response).isEqualTo(actual);


    }

}
