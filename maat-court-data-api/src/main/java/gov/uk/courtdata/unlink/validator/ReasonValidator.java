package gov.uk.courtdata.unlink.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.validator.IValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ReasonValidator implements IValidator<Void, Integer> {


    /**
     * Validate the passed in json.
     *
     * @param reasonId
     * @return
     * @throws ValidationException
     */
    @Override
    public Optional<Void> validate(Integer reasonId) {

        if (reasonId != null && reasonId > 0) {
            return Optional.empty();
        } else {
            throw new ValidationException("Reason id is missing.");
        }

    }
}
