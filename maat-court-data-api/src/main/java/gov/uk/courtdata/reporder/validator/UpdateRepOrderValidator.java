package gov.uk.courtdata.reporder.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.validator.IValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@XRayEnabled
@AllArgsConstructor
public class UpdateRepOrderValidator implements IValidator<Void, UpdateRepOrder> {

    private MaatIdValidator maatIdValidator;

    @Override
    public Optional<Void> validate(UpdateRepOrder updateRepOrder) {

        if (updateRepOrder.getRepId() == null) {
            throw new ValidationException("Rep Id is missing from request and is required");
        }
        maatIdValidator.validate(updateRepOrder.getRepId());

        return Optional.empty();
    }
}
