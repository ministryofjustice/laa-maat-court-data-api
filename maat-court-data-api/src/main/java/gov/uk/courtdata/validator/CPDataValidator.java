package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.RepOrderCPDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.RepOrderCPDataRepository;
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
    public Optional<Void> validate(CaseDetails caseDetails) throws ValidationException {

        Optional.ofNullable(caseDetails.getCaseUrn()).orElseThrow(
                () -> new ValidationException("CaseURN can't be null or empty on request."));

        Optional<RepOrderCPDataEntity> repOrderCPDataEntity = repOrderCPDataRepository.findByrepOrderId(caseDetails.getMaatId());

        repOrderCPDataEntity.orElseThrow(
                () -> new ValidationException(format("MaatId %s has no rep order cp data.", caseDetails.getMaatId())));

        Optional.ofNullable(repOrderCPDataEntity.get().getCaseUrn()).orElseThrow(
                () -> new ValidationException(format("MAATId: %s has not caseURN entered on MAAT application", caseDetails.getMaatId())));

        if (!repOrderCPDataEntity.get().getCaseUrn().equalsIgnoreCase(caseDetails.getCaseUrn())) {
            throw new ValidationException("CaseURN on request doesn't match with that on MAAT application.");
        }


        return Optional.empty();
    }
}
