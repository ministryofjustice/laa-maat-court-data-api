package gov.uk.courtdata.hardship.validator;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HardshipReviewValidationProcessor {

    private final HardshipReviewIdValidator hardshipReviewIdValidator;

    public Optional<Void> validate(final Integer hardshipId) {
        return hardshipReviewIdValidator.validate(hardshipId);
    }
}
