package gov.uk.courtdata.hardship.validator;

import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.enums.HardshipReviewStatus;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
public class UpdateHardshipReviewValidator implements IValidator<Void, UpdateHardshipReview> {

    private final HardshipReviewRepository hardshipReviewRepository;

    @Override
    @Transactional(rollbackFor = MAATCourtDataException.class)
    public Optional<Void> validate(final UpdateHardshipReview updateHardshipReview) {
        HardshipReviewEntity existing = hardshipReviewRepository.getById(updateHardshipReview.getId());

        LocalDateTime timestamp = existing.getUpdated() != null ? existing.getUpdated() : existing.getDateCreated();

        if (Math.abs(Duration.between(updateHardshipReview.getUpdated(), timestamp).getSeconds()) > 0) {
            throw new ValidationException("Hardship has been modified by another user");
        }

        if (existing.getStatus().equals(HardshipReviewStatus.COMPLETE)) {
            throw new ValidationException("User cannot modify a complete hardship review");
        }
        return Optional.empty();
    }
}
