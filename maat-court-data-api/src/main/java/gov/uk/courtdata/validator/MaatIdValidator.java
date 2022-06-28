package gov.uk.courtdata.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
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
@XRayEnabled
@Component
@RequiredArgsConstructor
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
                throw new ValidationException(String.format("MAAT/REP ID: %d is invalid.", maatId));
            return Optional.empty();

        } else {
            throw new ValidationException("MAAT ID is required.");
        }

    }
}
