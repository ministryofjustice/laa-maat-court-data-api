package gov.uk.courtdata.hardship.validator;

import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.hardship.CreateHardshipReview;
import gov.uk.courtdata.model.hardship.HardshipReview;
import gov.uk.courtdata.model.hardship.UpdateHardshipReview;
import gov.uk.courtdata.validator.HardshipReviewIdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HardshipReviewValidationProcessor {

    private final HardshipReviewIdValidator hardshipReviewIdValidator;
    private final CreateHardshipReviewValidator createHardshipReviewValidator;
    private final UpdateHardshipReviewValidator updateHardshipReviewValidator;

    public Optional<Void> validate(Integer hardshipId) {
        return hardshipReviewIdValidator.validate(hardshipId);
    }

    public Optional<Void> validate(HardshipReview hardshipReview) {

        if (hardshipReview == null) {
            throw new ValidationException("Hardship review Request is empty");
        }

        if (hardshipReview instanceof CreateHardshipReview) {
            return createHardshipReviewValidator.validate((CreateHardshipReview) hardshipReview);
        }
        if (hardshipReview instanceof UpdateHardshipReview) {
            hardshipReviewIdValidator.validate(((UpdateHardshipReview) hardshipReview).getId());
            return updateHardshipReviewValidator.validate((UpdateHardshipReview) hardshipReview);
        }

        return Optional.empty();
    }
}
