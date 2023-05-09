package gov.uk.courtdata.contribution.validator;

import gov.uk.courtdata.enums.ContributionsTransferStatus;
import gov.uk.courtdata.exception.ValidationException;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ContributionsValidator {
    protected Optional<Void> validateTransferStatus(String transferStatus) {
        if (EnumUtils.isValidEnum(ContributionsTransferStatus.class, transferStatus)) {
            return Optional.empty();
        } else {
            throw new ValidationException(String.format("Transfer Status: %s is invalid.", transferStatus));
        }
    }
}
