package gov.uk.courtdata.preupdatechecks.validator;

import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreUpdateChecksValidationProcessor {

    private final MaatIdValidator maatIdValidator;
    public Optional<Void> validate(Integer repId) {
        return maatIdValidator.validate(repId);
    }

}
