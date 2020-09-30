package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
import gov.uk.courtdata.validator.IValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
@Component
public class CPDataValidator implements IValidator<Void, CaseDetails> {

    private final RepOrderCPDataRepository repOrderCPDataRepository;

    @Override
    public Optional<Void> validate(CaseDetails caseDetails) {

        Optional<RepOrderCPDataEntity> repOrderCPDataEntity = repOrderCPDataRepository.findByrepOrderId(caseDetails.getMaatId());

        if (repOrderCPDataEntity.isEmpty())
            throw new ValidationException(
                    format("MaatId %s has no common platform data created in MAAT.", caseDetails.getMaatId()));

        return Optional.empty();
    }
}
