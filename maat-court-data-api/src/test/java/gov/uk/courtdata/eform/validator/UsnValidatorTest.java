package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.service.EformStagingDAO;
import gov.uk.courtdata.exception.UsnValidationException;
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
    private EformStagingDAO mockEformStagingDAO;

    @InjectMocks
    private UsnValidator usnValidator;

    @Test
    void shouldThrowValidationException_When_Calling_VerifyUsnExists_withNonexistentUsn() {
        when(mockEformStagingDAO.isUsnPresentInDB(654321))
                .thenReturn(false);

        UsnValidationException exception = Assertions.assertThrows(
                UsnValidationException.class, () -> usnValidator.verifyUsnExists(654321));

        assertEquals("The USN [654321] is not valid.", exception.getMessage());
    }

    @Test
    void shouldNotThrowValidationException_When_Calling_VerifyUsnExists_withValidatingValidUsn() {
        when(mockEformStagingDAO.isUsnPresentInDB(123))
                .thenReturn(true);

        usnValidator.verifyUsnExists(123);
    }

    @Test
    void shouldNotThrowValidationException_When_Calling_VerifyUsnDoesNotExist_withNonexistentUsn() {
        when(mockEformStagingDAO.isUsnPresentInDB(654321))
                .thenReturn(true);

        UsnValidationException exception = Assertions.assertThrows(
                UsnValidationException.class, () -> usnValidator.verifyUsnDoesNotExist(654321));

        assertEquals("The USN [654321] is not valid.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationException_When_Calling_VerifyUsnDoesNotExist_withValidatingValidUsn() {
        when(mockEformStagingDAO.isUsnPresentInDB(123))
                .thenReturn(false);

        usnValidator.verifyUsnDoesNotExist(123);
    }
}