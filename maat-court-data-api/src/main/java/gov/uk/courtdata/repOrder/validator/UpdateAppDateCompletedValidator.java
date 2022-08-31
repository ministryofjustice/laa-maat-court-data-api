package gov.uk.courtdata.repOrder.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
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
public class UpdateAppDateCompletedValidator implements IValidator<Void, UpdateAppDateCompleted> {

    private MaatIdValidator maatIdValidator;

    @Override
    public Optional<Void> validate(UpdateAppDateCompleted updateAppDateCompleted) {

        if (updateAppDateCompleted.getRepId() == null) {
            throw new ValidationException("Rep Id is missing from request and is required");
        } else if (updateAppDateCompleted.getAssessmentDateCompleted() == null) {
            throw new ValidationException("Assessment Date completed is missing from request and is required");
        }
        maatIdValidator.validate(updateAppDateCompleted.getRepId());

        return Optional.empty();
    }
}
