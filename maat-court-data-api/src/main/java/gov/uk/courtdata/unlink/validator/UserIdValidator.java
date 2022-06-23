package gov.uk.courtdata.unlink.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.validator.IValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@XRayEnabled
@RequiredArgsConstructor
public class UserIdValidator implements IValidator<Void, String> {

    @Override
    public Optional<Void> validate(String userId) {

        if (userId != null && !userId.isEmpty()) {
            return Optional.empty();
        } else {
            throw new ValidationException("User id is missing.");
        }

    }
}
