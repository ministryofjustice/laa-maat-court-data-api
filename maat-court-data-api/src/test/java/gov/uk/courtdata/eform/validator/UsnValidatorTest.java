package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.exception.USNValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsnValidatorTest {

    @Mock
    private EformStagingRepository mockEformStagingRepository;

    @InjectMocks
    private UsnValidator usnValidator;

    @Test
    void shouldThrowValidationException_When_Calling_VerifyUsnExists_withNullUsn() {
        when(mockEformStagingRepository.existsById(null))
                .thenReturn(false);

        USNValidationException exception = Assertions.assertThrows(
                USNValidationException.class, () -> usnValidator.verifyUsnExists(null));

        assertEquals("The USN number [null] is not valid as it is not present in the eForm Repository", exception.getMessage());
    }

    @Test
    void shouldThrowValidationException_When_Calling_VerifyUsnExists_withNonexistentUsn() {
        when(mockEformStagingRepository.existsById(654321))
                .thenReturn(false);

        USNValidationException exception = Assertions.assertThrows(
                USNValidationException.class, () -> usnValidator.verifyUsnExists(654321));

        assertEquals("The USN number [654321] is not valid as it is not present in the eForm Repository", exception.getMessage());
    }

    @Test
    void shouldNotThrowValidationException_When_Calling_VerifyUsnExists_withValidatingValidUsn() {
        when(mockEformStagingRepository.existsById(123))
                .thenReturn(true);

        usnValidator.verifyUsnExists(123);
    }
}