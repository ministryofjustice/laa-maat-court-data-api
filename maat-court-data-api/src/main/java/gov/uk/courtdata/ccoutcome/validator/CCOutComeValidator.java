package gov.uk.courtdata.ccoutcome.validator;

import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CCOutComeValidator {

    private final MaatIdValidator maatIdValidator;
    private final CreateCCOutComeValidator createCCOutComeValidator;
    private final UpdateCCOutComeValidator updateCCOutComeValidator;

    public Optional<Void> validate(RepOrderCCOutCome repOrderCCOutCome) {

        if (null != repOrderCCOutCome.getId() && repOrderCCOutCome.getId() > 0) {
            updateCCOutComeValidator.validate(repOrderCCOutCome);
            maatIdValidator.validate(repOrderCCOutCome.getRepId());
        } else {
            createCCOutComeValidator.validate(repOrderCCOutCome);
            maatIdValidator.validate(repOrderCCOutCome.getRepId());
        }
        return Optional.empty();
    }

    public Optional<Void> validate(Integer repId) {
        maatIdValidator.validate(repId);
        return Optional.empty();
    }

}
