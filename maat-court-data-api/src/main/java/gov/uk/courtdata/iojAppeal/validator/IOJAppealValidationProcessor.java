package gov.uk.courtdata.iojAppeal.validator;

import gov.uk.courtdata.validator.IOJAppealIdValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IOJAppealValidationProcessor {

    private final IOJAppealIdValidator iojAppealIdValidator;

    public Optional<Void> validate(Integer iojAppealId) {
        return iojAppealIdValidator.validate(iojAppealId);
    }
}
