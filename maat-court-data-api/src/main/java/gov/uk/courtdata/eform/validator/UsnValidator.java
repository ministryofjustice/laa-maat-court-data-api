package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.service.EformStagingDAO;
import gov.uk.courtdata.exception.USNValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The responsibility of this class it to verify that the USN provided is valid by checking that exists in
 * the EformStagingRepository.
 */
@Component
@RequiredArgsConstructor
public class UsnValidator {

    private final EformStagingDAO eformStagingDAO;

    public void verifyUsnExists(Integer usn) {
        if (!eformStagingDAO.isUsnPresentInDB(usn)) {
            String message = String.format("The USN number [%d] is not valid.", usn);
            throw new USNValidationException(message);
        }
    }

    public void verifyUsnDoesNotExist(Integer usn) {
        if (eformStagingDAO.isUsnPresentInDB(usn)) {
            String message = String.format("The USN number [%d] is not valid.", usn);
            throw new USNValidationException(message);
        }
    }
}
