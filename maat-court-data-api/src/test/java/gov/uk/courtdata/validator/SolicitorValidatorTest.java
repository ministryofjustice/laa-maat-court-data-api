package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SolicitorValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private SolicitorMAATDataRepository solicitorMAATDataRepository;

    @InjectMocks
    private SolicitorValidator solicitorValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testWhenSolicitorDetailsNotFound_throwsException() {

        final int testMaatId = 1000;
        thrown.expect(ValidationException.class);
        thrown.expectMessage(format("Solicitor not found for maatId %s", testMaatId));
        when(solicitorMAATDataRepository.findBymaatId(testMaatId)).thenReturn(Optional.empty());
        solicitorValidator.validate(testMaatId);


    }

    @Test
    public void testWhenSolicitorAccountCodeNotAvailable_throwsException() {

        final int testMaatId = 1000;
        thrown.expect(ValidationException.class);
        thrown.expectMessage(format("Solicitor account code not available for maatId %s.", testMaatId));
        when(solicitorMAATDataRepository.findBymaatId(testMaatId))
                .thenReturn(Optional.of(SolicitorMAATDataEntity.builder().maatId(testMaatId).accountCode("  ").build()));
        solicitorValidator.validate(testMaatId);


    }

    @Test
    public void testWhenSolicitorDetailsExists_validationPasses() {
        final int testMaatId = 1000;
        when(solicitorMAATDataRepository.findBymaatId(testMaatId))
                .thenReturn(Optional.of(SolicitorMAATDataEntity.builder().maatId(testMaatId).accountCode("SOC1212").build()));
        solicitorValidator.validate(testMaatId);
    }
}
