package gov.uk.courtdata.contributions.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.validator.IValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateContributionsValidator extends ContributionsValidator implements IValidator<Void, CreateContributions> {

    private final MaatIdValidator maatIdValidator;

    @Override
    public Optional<Void> validate(CreateContributions createContributions) {
        validateApplId(createContributions.getApplId());
        maatIdValidator.validate(createContributions.getRepId());
        validateUserCreated(createContributions.getUserCreated());
        validateEffectiveDate(createContributions.getEffectiveDate());
        validateCalcDate(createContributions.getCalcDate());
        validateContributionsCap(createContributions.getContributionCap());
        validateMonthlyContribs(createContributions.getMonthlyContributions());
        validateTransferStatus(createContributions.getTransferStatus());
        return Optional.empty();
    }

    private Optional<Void> validateApplId(Integer applId) {
        if (applId == null) {
            throw new ValidationException(String.format("Appl Id: %d is invalid.", applId));
        } else {
            return Optional.empty();
        }
    }

    private Optional<Void> validateUserCreated(String userCreated) {
        if (userCreated.isBlank()) {
            throw new ValidationException(String.format("User Created: %d is invalid.", userCreated));
        } else {
            return Optional.empty();
        }
    }
}
