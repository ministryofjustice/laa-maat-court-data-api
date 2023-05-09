package gov.uk.courtdata.contribution.validator;

import gov.uk.courtdata.contribution.model.CreateContributions;
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
        maatIdValidator.validate(createContributions.getRepId());
        validateTransferStatus(createContributions.getTransferStatus());
        return Optional.empty();
    }
}
