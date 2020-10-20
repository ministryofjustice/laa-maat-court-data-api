package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.RepOrderRepository;
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

    private final RepOrderRepository repOrderRepository;

    /**
     * @param maatId
     * @return
     * @throws ValidationException
     */
    @Override
    public Optional<Void> validate(final Integer maatId) {

        if (maatId != null && maatId > 0) {
            Optional<RepOrderEntity> repOrderEntity = repOrderRepository.findById(maatId);
            if (repOrderEntity.isEmpty())
                throw new ValidationException(maatId + " is invalid.");
            return Optional.empty();

        } else {
            throw new ValidationException("MAAT id is required.");
        }

    }
}
