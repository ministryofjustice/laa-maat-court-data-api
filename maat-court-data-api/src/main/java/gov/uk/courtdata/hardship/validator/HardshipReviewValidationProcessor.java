package gov.uk.courtdata.hardship.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HardshipReviewValidationProcessor {

    private final HardshipReviewIdValidator hardshipReviewIdValidator;

    public Optional<Void> validate(final Integer hardshipId) {
        return hardshipReviewIdValidator.validate(hardshipId);
    }
}
