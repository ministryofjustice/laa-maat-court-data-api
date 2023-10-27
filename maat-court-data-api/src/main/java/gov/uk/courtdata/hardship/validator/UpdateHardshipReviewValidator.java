package gov.uk.courtdata.hardship.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.HardshipReviewEntity;
import gov.uk.courtdata.enums.HardshipReviewStatus;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Component
@XRayEnabled
public class UpdateHardshipReviewValidator implements IValidator<Void, UpdateHardshipReview> {

    private final HardshipReviewRepository hardshipReviewRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Void> validate(final UpdateHardshipReview updateHardshipReview) {
        HardshipReviewEntity existing = hardshipReviewRepository.getReferenceById(updateHardshipReview.getId());

        if (existing.getStatus().equals(HardshipReviewStatus.COMPLETE)) {
            throw new ValidationException("User cannot modify a complete hardship review");
        }
        return Optional.empty();
    }
}
