package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.repository.CourtHouseCodesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReferenceDataValidatorTest {
    @Mock
    private CourtHouseCodesRepository courtHouseCodesRepository;

    @InjectMocks
    private ReferenceDataValidator referenceDataValidator;

    @Test
    public void testWhenCourtLocationNotFound_throwsException() {
        when(courtHouseCodesRepository.getCount("B1J10")).thenReturn(0);
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () ->
                referenceDataValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16")
                        .sessions(Arrays.asList(Session.builder().courtLocation("B1J10").build()))
                        .build()));
        assertThat(validationException.getMessage()).isEqualTo("Court location not found B1J10");

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
        ValidationException validationException = Assertions.assertThrows(ValidationException.class, () ->
                referenceDataValidator.validate(CaseDetails.builder().maatId(100).cjsAreaCode("16")
                        .sessions(Arrays.asList(Session.builder().courtLocation("B1J10").build(),
                                Session.builder().courtLocation("B2J10").build()))
                        .build()));
        assertThat(validationException.getMessage()).isEqualTo("Court location not found B1J10");
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
