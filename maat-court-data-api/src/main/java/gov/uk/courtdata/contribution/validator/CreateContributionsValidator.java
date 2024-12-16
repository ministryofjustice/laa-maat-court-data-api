package gov.uk.courtdata.contribution.validator;

import gov.uk.courtdata.validator.IValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.gov.justice.laa.crime.common.model.contribution.maat_api.CreateContributionRequest;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateContributionsValidator implements IValidator<Void, CreateContributionRequest> {

    private final MaatIdValidator maatIdValidator;

    @Override
    public Optional<Void> validate(CreateContributionRequest createContributions) {
        maatIdValidator.validate(createContributions.getRepId());
        return Optional.empty();
    }
}
