package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.repository.CourtHouseCodesRepository;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataValidatorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private CourtHouseCodesRepository courtHouseCodesRepository;

    @InjectMocks
    private ReferenceDataValidator referenceDataValidator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWhenCourtLocationNotFound_throwsException() {

        when(courtHouseCodesRepository.getCount("B1J10")).thenReturn(0);
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Court location not found B1J10");

        referenceDataValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16")
                .sessions(Arrays.asList(Session.builder().courtLocation("B1J10").build()))
                .build());
    }

    @Test
    public void testWhenCourtLocationExists_validationPasses() {

        when(courtHouseCodesRepository.getCount("B1J10")).thenReturn(1);

        referenceDataValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16")
                .sessions(Arrays.asList(Session.builder().courtLocation("B1J10").build()))
                .build());

    }

    @Test
    public void testWhenMultiSessionCourtAnyMissing_throwsException() {

        when(courtHouseCodesRepository.getCount(anyString())).thenReturn(0);
        thrown.expect(ValidationException.class);
        thrown.expectMessage("Court location not found B1J10");

        referenceDataValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16")
                .sessions(Arrays.asList(Session.builder().courtLocation("B1J10").build(),
                        Session.builder().courtLocation("B2J10").build()))
                .build());

    }

    @Test
    public void testWhenMultipleSessions_courtLocationValidated() {

        when(courtHouseCodesRepository.getCount(anyString())).thenReturn(1);

        referenceDataValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16")
                .sessions(Arrays.asList(Session.builder().courtLocation("B1J10").build(),
                        Session.builder().courtLocation("B2J10").build()))
                .build());

    }
}
