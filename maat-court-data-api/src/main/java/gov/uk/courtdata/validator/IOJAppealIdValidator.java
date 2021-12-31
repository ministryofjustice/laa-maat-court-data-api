package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.IOJAppealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class IOJAppealIdValidator implements IValidator<Void, Integer> {

    private final IOJAppealRepository iojAppealRepository;

    @Override
    public Optional<Void> validate(final Integer iojAppealId) {

        if (isValid(iojAppealId)) {
            iojAppealRepository.findById(iojAppealId).orElseThrow(()-> new ValidationException("IOJ Appeal id " + iojAppealId + " is invalid"));

            return Optional.empty();
        }

        throw new ValidationException("Valid IOJ Appeal id is required");
    }

    private boolean isValid(Integer value) {
        return value != null && value > 0;
    }
}
