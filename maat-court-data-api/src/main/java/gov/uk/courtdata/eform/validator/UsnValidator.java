package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.service.EformStagingService;
import gov.uk.courtdata.exception.UsnValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The responsibility of this class it to verify that the USN provided is valid by checking that exists in
 * the EformStagingRepository.
 */
@Component
@RequiredArgsConstructor
public class UsnValidator {

    private static final String EXCEPTION_MESSAGE_FORMAT = "The USN [%d] is not valid.";

    private final EformStagingService eformStagingService;

    public void verifyUsnExists(Integer usn) {
        if (!eformStagingService.isUsnPresentInDB(usn)) {
            String message = String.format(EXCEPTION_MESSAGE_FORMAT, usn);
            throw new UsnValidationException(message);
        }
    }

    public void verifyUsnDoesNotExist(Integer usn) {
        if (eformStagingService.isUsnPresentInDB(usn)) {
            String message = String.format(EXCEPTION_MESSAGE_FORMAT, usn);
            throw new UsnValidationException(message);
        }
    }
}
