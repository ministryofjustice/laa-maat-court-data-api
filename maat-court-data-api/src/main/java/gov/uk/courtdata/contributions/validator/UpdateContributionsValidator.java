package gov.uk.courtdata.contributions.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.contributions.UpdateContributions;
import gov.uk.courtdata.repository.ContributionsRepository;
import gov.uk.courtdata.validator.IValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UpdateContributionsValidator extends ContributionsValidator implements IValidator<Void, UpdateContributions> {

    private final ContributionsRepository contributionsRepository;

    @Override
    public Optional<Void> validate(UpdateContributions updateContributions) {
        validateId(updateContributions.getId());
        validateTransferStatus(updateContributions.getTransferStatus());
        return Optional.empty();
    }

    private Optional<Void> validateId(Integer id) {
        if (id != null && id > 0) {
            boolean contributionsExists = contributionsRepository.existsById(id);
            if (!contributionsExists) {
                throw new ValidationException(String.format("Contributions ID: %d is invalid.", id));
            }
            return Optional.empty();
        } else {
            throw new ValidationException("Contributions ID is required.");
        }
    }
}
