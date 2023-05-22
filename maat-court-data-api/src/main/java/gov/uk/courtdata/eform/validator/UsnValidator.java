package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.service.EformStagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The responsibility of this class it to verify that the USN provided is valid by checking that exists in
 * the EformStagingRepository.
 */
@Component
@RequiredArgsConstructor
public class UsnValidator {

    private static final String ALREADY_EXISTS_MESSAGE_FORMAT = "The USN [%d] already exists in the data store.";
    private static final String NONEXISTENT_MESSAGE_FORMAT = "The USN [%d] does not exist in the data store.";

    private final EformStagingService eformStagingService;

    public void verifyUsnExists(int usn) throws UsnException {
        if (!eformStagingService.isUsnPresentInDB(usn)) {
            throw UsnException.nonexistent(String.format(NONEXISTENT_MESSAGE_FORMAT, usn));
        }
    }

    public void verifyUsnDoesNotExist(int usn) throws UsnException {
        if (eformStagingService.isUsnPresentInDB(usn)) {
            throw UsnException.alreadyExists(String.format(ALREADY_EXISTS_MESSAGE_FORMAT, usn));
        }
    }
}
