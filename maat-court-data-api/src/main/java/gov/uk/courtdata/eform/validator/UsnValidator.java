package gov.uk.courtdata.eform.validator;

import gov.uk.courtdata.eform.repository.EformStagingRepository;
import gov.uk.courtdata.exception.USNValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsnValidator {

    private final EformStagingRepository eformStagingRepository;

    public void validate(Integer usn) {
        if (!eformStagingRepository.existsById(usn)) {
            throw new USNValidationException("The USN number entered is not valid.");
        }
        // TODO
    }
}
