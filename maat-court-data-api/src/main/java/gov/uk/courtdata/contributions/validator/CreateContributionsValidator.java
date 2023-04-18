package gov.uk.courtdata.contributions.validator;

import gov.uk.courtdata.model.contributions.CreateContributions;
import gov.uk.courtdata.validator.IValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateContributionsValidator extends ContributionsValidator implements IValidator<Void, CreateContributions> {

    @Override
    public Optional<Void> validate(CreateContributions createContributions) {
        // Check appl_id is not null (create)
        // Check rep_id is not null (create)
        // Check user_created is not null (create)
        // Check effective date is not null
        // Check calc date is not null
        // Check contributions cap is not null
        // Check monthly contributions is not null
        // Check transfer status is enum value
        return Optional.empty();
    }

    private Optional<Void> validateApplId(Integer applId) {
        return Optional.empty();
    }

    private Optional<Void> validateRepId(Integer repId) {
        return Optional.empty();
    }

    private Optional<Void> userCreated(String userCreated) {
        return Optional.empty();
    }
}
