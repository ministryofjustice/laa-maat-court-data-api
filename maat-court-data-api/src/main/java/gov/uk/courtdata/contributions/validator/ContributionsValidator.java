package gov.uk.courtdata.contributions.validator;

import gov.uk.courtdata.enums.ContributionsTransferStatus;
import gov.uk.courtdata.exception.ValidationException;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class ContributionsValidator {
    public Optional<Void> validateTransferStatus(String transferStatus) {
        if (EnumUtils.isValidEnum(ContributionsTransferStatus.class, transferStatus)) {
            return Optional.empty();
        } else {
            throw new ValidationException(String.format("Transfer Status: %d is invalid.", transferStatus));
        }
    }
}
