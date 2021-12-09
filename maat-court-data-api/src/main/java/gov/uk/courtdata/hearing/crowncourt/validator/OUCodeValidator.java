package gov.uk.courtdata.hearing.crowncourt.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hearing.HearingResulted;
import gov.uk.courtdata.validator.IValidator;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Component

//todo: delete me
public class OUCodeValidator implements IValidator<Void, HearingResulted> {

    @Override
    public Optional<Void> validate(HearingResulted hearingResulted) {

        String ouCode = hearingResulted.getSession().getCourtLocation();

        if (!isBlank(ouCode)) {
            return Optional.empty();
        } else {
            throw new ValidationException("OU Code is missing.");
        }

    }
}
