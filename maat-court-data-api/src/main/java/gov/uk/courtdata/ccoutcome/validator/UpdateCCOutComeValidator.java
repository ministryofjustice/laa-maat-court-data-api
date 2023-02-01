package gov.uk.courtdata.ccoutcome.validator;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.entity.RepOrderCCOutComeEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.ccoutcome.RepOrderCCOutCome;
import gov.uk.courtdata.repository.CrownCourtProcessingRepository;
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
public class UpdateCCOutComeValidator implements IValidator<Void, RepOrderCCOutCome> {

    private final CrownCourtProcessingRepository courtProcessingRepository;

    @Override
    public Optional<Void> validate(RepOrderCCOutCome repOrderCCOutCome) {

        Optional<RepOrderCCOutComeEntity> repOrderCCOutComeEntity = courtProcessingRepository.findById(repOrderCCOutCome.getId());

        if (isBlank(repOrderCCOutCome.getCrownCourtCode())) {
            throw new ValidationException("Crown Court Code is required");
        } else if (isBlank(repOrderCCOutCome.getCaseNumber())) {
            throw new ValidationException("CaseNumber is required");
        } else if (isBlank(repOrderCCOutCome.getUserModified())) {
            throw new ValidationException("User modified is required");
        } else if (repOrderCCOutComeEntity.isEmpty()) {
            throw new ValidationException(String.format("RepOrder CCOutcome: %d is invalid.", repOrderCCOutCome.getId()));
        }

        return Optional.empty();
    }
}
