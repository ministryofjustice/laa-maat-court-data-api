package gov.uk.courtdata.validator;

import gov.uk.courtdata.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <class>MaatIdValidator</class> validate maat id exists.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MaatIdValidator implements IValidator<Void, Integer> {


    /**
     *
     * @param maatId
     * @return
     * @throws ValidationException
     */
    @Override
    public Optional<Void> validate(final Integer maatId) {

        Optional.ofNullable(maatId).orElseThrow(() -> new ValidationException("MAAT id is missing."));

        return Optional.empty();
    }
}
