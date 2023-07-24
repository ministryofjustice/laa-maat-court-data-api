package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.exception.USNExceptionUtil;
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

    private final EformStagingService eformStagingService;

    public void verifyUsnExists(int usn) {
        if (!eformStagingService.isUsnPresentInDB(usn)) {
            throw USNExceptionUtil.nonexistent(usn);
        }
    }

    public void verifyUsnDoesNotExist(int usn) {
        if (eformStagingService.isUsnPresentInDB(usn)) {
            throw USNExceptionUtil.alreadyExists(usn);
        }
    }
}
