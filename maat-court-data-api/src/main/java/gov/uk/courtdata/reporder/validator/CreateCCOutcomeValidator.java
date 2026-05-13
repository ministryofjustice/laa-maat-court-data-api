package gov.uk.courtdata.reporder.validator;

import static org.apache.commons.lang3.StringUtils.isBlank;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CreateCCOutcomeValidator implements IValidator<Void, RepOrderCCOutcome> {

    @Override
    public Optional<Void> validate(RepOrderCCOutcome repOrderCCOutCome) {

        if (isBlank(repOrderCCOutCome.getUserCreated())) {
            throw new ValidationException("User created is required");
        }
        return Optional.empty();
    }
}
