package gov.uk.courtdata.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.Session;
import gov.uk.courtdata.repository.CourtHouseCodesRepository;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReferenceDataValidatorTest {
    @Mock
    private CourtHouseCodesRepository courtHouseCodesRepository;

    @InjectMocks
    private ReferenceDataValidator referenceDataValidator;

    @Test
    void testWhenCourtLocationNotFound_throwsException() {
        when(courtHouseCodesRepository.getCount("B1J10")).thenReturn(0);
        var caseDetail = CaseDetails.builder()
                .maatId(100)
                .cjsAreaCode("16")
                .sessions(Arrays.asList(Session.builder().courtLocation("B1J10").build()))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> referenceDataValidator.validate(caseDetail));
        assertThat(validationException.getMessage()).isEqualTo("Court location not found B1J10");
    }

    @Test
    void testWhenCourtLocationExists_validationPasses() {
        when(courtHouseCodesRepository.getCount("B1J10")).thenReturn(1);
        referenceDataValidator.validate(CaseDetails.builder()
                .maatId(100)
                .cjsAreaCode("16")
                .sessions(Arrays.asList(Session.builder().courtLocation("B1J10").build()))
                .build());
    }

    @Test
    void testWhenMultiSessionCourtAnyMissing_throwsException() {
        when(courtHouseCodesRepository.getCount(anyString())).thenReturn(0);
        var caseDetail = CaseDetails.builder()
                .maatId(100)
                .cjsAreaCode("16")
                .sessions(Arrays.asList(
                        Session.builder().courtLocation("B1J10").build(),
                        Session.builder().courtLocation("B2J10").build()))
                .build();
        ValidationException validationException =
                Assertions.assertThrows(ValidationException.class, () -> referenceDataValidator.validate(caseDetail));
        assertThat(validationException.getMessage()).isEqualTo("Court location not found B1J10");
    }

    @Test
    void testWhenMultipleSessions_courtLocationValidated() {
        when(courtHouseCodesRepository.getCount(anyString())).thenReturn(1);
        referenceDataValidator.validate(CaseDetails.builder()
                .maatId(100)
                .cjsAreaCode("16")
                .sessions(Arrays.asList(
                        Session.builder().courtLocation("B1J10").build(),
                        Session.builder().courtLocation("B2J10").build()))
                .build());
    }
}
