package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SolicitorValidator implements IValidator<SolicitorMAATDataEntity, CaseDetails> {

    private final SolicitorMAATDataRepository solicitorMAATDataRepository;

    /**
     * @param caseDetailsJson
     * @return
     * @throws ValidationException
     */
    @Override
    public Optional<SolicitorMAATDataEntity> validate(CaseDetails caseDetailsJson) throws ValidationException {

        // Get the solicitor details.

        Optional<SolicitorMAATDataEntity> solicitorViewEntity =
                solicitorMAATDataRepository.findBymaatId(caseDetailsJson.getMaatId());

        solicitorViewEntity.orElseThrow(() -> new ValidationException("Solicitor not found."));


        Optional.ofNullable(solicitorViewEntity.get().getAccountCode())
                .orElseThrow(() -> new ValidationException("Solicitor account code is null."));

        return solicitorViewEntity;
    }

}
