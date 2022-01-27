package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class HardshipReviewIdValidator implements IValidator<Void, Integer> {

    private final HardshipReviewRepository hardshipReviewRepository;

    @Override
    public Optional<Void> validate(final Integer hardshipId) {

        if (hardshipId != null && hardshipId > 0) {
            Optional<HardshipReviewEntity> hardshipEntity = hardshipReviewRepository.findById(hardshipId);
            if (hardshipEntity.isEmpty())
                throw new ValidationException(hardshipId + " is invalid");
            return Optional.empty();

        } else {
            throw new ValidationException("Hardship review id is required");
        }

    }
}
