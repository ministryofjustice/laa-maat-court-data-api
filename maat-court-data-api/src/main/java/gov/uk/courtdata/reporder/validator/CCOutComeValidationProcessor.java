package gov.uk.courtdata.reporder.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.model.RepOrderCCOutcome;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@XRayEnabled
public class CCOutComeValidationProcessor {

    private final MaatIdValidator maatIdValidator;
    private final CreateCCOutcomeValidator createCCOutComeValidator;

    public Optional<Void> validate(RepOrderCCOutcome repOrderCCOutCome) {

        if (null == repOrderCCOutCome.getId() || repOrderCCOutCome.getId() == 0) {
            createCCOutComeValidator.validate(repOrderCCOutCome);
        }
        return maatIdValidator.validate(repOrderCCOutCome.getRepId());
    }

    public Optional<Void> validate(Integer repId) {
        return maatIdValidator.validate(repId);
    }

}
