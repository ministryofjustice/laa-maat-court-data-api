package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.repository.EformStagingRepository;
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

    private final EformStagingRepository eformStagingRepository;

    public void validate(Integer usn) {
        if (!eformStagingRepository.existsById(usn)) {
            String message = String.format("The USN number [%d] is not valid as it is not present in the eForm Repository", usn);
            throw new USNValidationException(message);
        }
    }
}
