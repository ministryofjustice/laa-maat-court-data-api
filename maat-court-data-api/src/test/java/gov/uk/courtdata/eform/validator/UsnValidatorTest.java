package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.service.EformStagingService;
import gov.uk.courtdata.exception.UsnException;
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
    private EformStagingService mockEformStagingService;

    @InjectMocks
    private UsnValidator usnValidator;

    @Test
    void shouldThrowUsnException_When_Calling_VerifyUsnExists_withNonexistentUsn() {
        when(mockEformStagingService.isUsnPresentInDB(654321))
                .thenReturn(false);

        UsnException exception = Assertions.assertThrows(
                UsnException.class, () -> usnValidator.verifyUsnExists(654321));

        assertEquals("The USN [654321] does not exist in the data store.", exception.getMessage());
    }

    @Test
    void shouldNotThrowValidationException_When_Calling_VerifyUsnExists_withValidatingValidUsn() {
        when(mockEformStagingService.isUsnPresentInDB(7000001))
                .thenReturn(true);

        usnValidator.verifyUsnExists(7000001);
    }

    @Test
    void shouldThrowUsnException_When_Calling_VerifyUsnDoesNotExist_withExistentUsn() {
        when(mockEformStagingService.isUsnPresentInDB(7000001))
                .thenReturn(true);

        UsnException exception = Assertions.assertThrows(
                UsnException.class, () -> usnValidator.verifyUsnDoesNotExist(7000001));

        assertEquals("The USN [7000001] already exists in the data store.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationException_When_Calling_VerifyUsnDoesNotExist_withValidatingValidUsn() {
        when(mockEformStagingService.isUsnPresentInDB(654321))
                .thenReturn(false);

        usnValidator.verifyUsnDoesNotExist(654321);
    }
}