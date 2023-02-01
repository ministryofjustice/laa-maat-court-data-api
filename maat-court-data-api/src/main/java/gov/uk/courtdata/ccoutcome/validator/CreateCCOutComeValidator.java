package gov.uk.courtdata.ccoutcome.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
import gov.uk.courtdata.validator.IValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Component
@XRayEnabled
@AllArgsConstructor
public class CreateCCOutComeValidator implements IValidator<Void, RepOrderCCOutCome> {


    @Override
    public Optional<Void> validate(RepOrderCCOutCome RepOrderCCOutCome) {


        if (isBlank(RepOrderCCOutCome.getUserCreated())) {
            throw new ValidationException("User created is required");
        } else if (isBlank(RepOrderCCOutCome.getCcooOutcome())) {
            throw new ValidationException("CC OutCome is required");
        } else if (null == RepOrderCCOutCome.getCcooOutcomeDate()) {
            throw new ValidationException("CC OutCome Date is required");
        }
        return Optional.empty();
    }
}
