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
    public Optional<Void> validateEffectiveDate(LocalDate effectiveDate) {
        if (effectiveDate == null) {
            throw new ValidationException(String.format("Effective Date: %d is invalid.", effectiveDate));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Void> validateCalcDate(LocalDate calcDate) {
        if (calcDate == null) {
            throw new ValidationException(String.format("Calc Date: %d is invalid.", calcDate));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Void> validateContributionsCap(BigDecimal contributionsCap) {
        if (contributionsCap == null) {
            throw new ValidationException(String.format("Contributions Cap: %d is invalid.", contributionsCap));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Void> validateMonthlyContribs(BigDecimal monthlyContribs) {
        if (monthlyContribs == null) {
            throw new ValidationException(String.format("Monthly Contribs: %d is invalid.", monthlyContribs));
        } else {
            return Optional.empty();
        }
    }

    public Optional<Void> validateTransferStatus(String transferStatus) {
        if (EnumUtils.isValidEnum(ContributionsTransferStatus.class, transferStatus)) {
            return Optional.empty();
        } else {
            throw new ValidationException(String.format("Transfer Status: %d is invalid.", transferStatus));
        }
    }

}
