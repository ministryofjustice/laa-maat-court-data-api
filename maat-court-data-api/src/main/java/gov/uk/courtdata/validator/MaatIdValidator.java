package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.reporder.service.RepOrderService;
import gov.uk.courtdata.repository.RepOrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MaatIdValidator implements IValidator<Void, Integer> {

    private final RepOrderRepository repOrderRepository;
    private final RepOrderService repOrderService;

    /**
     * Validate MAAT ID exists.
     */
    @Override
    public Optional<Void> validate(final Integer maatId) {

        if (maatId != null && maatId > 0) {
            Optional<RepOrderEntity> repOrderEntity = repOrderRepository.findById(maatId);
            if (repOrderEntity.isEmpty()) {
                String message = String.format("MAAT/REP ID [%d] is invalid", maatId);
                throw new ValidationException(message);
            }
            return Optional.empty();

        } else {
            String message = String.format("MAAT/REP ID is required, found [%d]", maatId);
            throw new ValidationException(message);
        }
    }

    /**
     * Validate MAAT ID does not exist.
     */
    public void validateNotExists(Integer maatId) {

        if (maatId != null && maatId > 0) {
            if (repOrderService.exists(maatId)) {
                String message = String.format("There is already a record with MAAT/REP ID [%d]",
                    maatId);
                throw new ValidationException(message);
            }

        } else {
            String message = String.format("MAAT/REP ID is required, found [%d]", maatId);
            throw new ValidationException(message);
        }
    }
}
