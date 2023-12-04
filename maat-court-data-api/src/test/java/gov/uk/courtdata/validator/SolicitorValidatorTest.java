package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SolicitorValidatorTest {

    @Mock
    private SolicitorMAATDataRepository solicitorMAATDataRepository;

    @InjectMocks
    private SolicitorValidator solicitorValidator;

    @Test
    public void testWhenSolicitorDetailsNotFound_throwsException() {
        int testMaatId = 1000;
        when(solicitorMAATDataRepository.findBymaatId(testMaatId)).thenReturn(Optional.empty());
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () ->
                solicitorValidator.validate(testMaatId));
        assertThat(validationException.getMessage()).isEqualTo(format("Solicitor not found for maatId %s", testMaatId));

    }

    @Test
    public void testWhenSolicitorAccountCodeNotAvailable_throwsException() {
        int testMaatId = 1000;
        when(solicitorMAATDataRepository.findBymaatId(testMaatId))
                .thenReturn(Optional.of(SolicitorMAATDataEntity.builder().maatId(testMaatId).accountCode("  ").build()));
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () ->
                solicitorValidator.validate(testMaatId));
        assertThat(validationException.getMessage()).isEqualTo(format("Solicitor account code not available for maatId %s.", testMaatId));
    }

    @Test
    public void testWhenSolicitorDetailsExists_validationPasses() {
        int testMaatId = 1000;
        when(solicitorMAATDataRepository.findBymaatId(testMaatId))
                .thenReturn(Optional.of(SolicitorMAATDataEntity.builder().maatId(testMaatId).accountCode("SOC1212").build()));
        solicitorValidator.validate(testMaatId);
    }
}
