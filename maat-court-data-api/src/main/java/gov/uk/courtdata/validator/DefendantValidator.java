package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.DefendantMAATDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 *<code>DefendantValidator</code> to check existence of MAAT defendant details.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DefendantValidator implements IValidator<DefendantMAATDataEntity, CaseDetails> {


    private final DefendantMAATDataRepository defendantMAATDataRepository;

    /**
     * @param caseDetailsJson
     * @return
     * @throws ValidationException
     */
    @Override
    public Optional<DefendantMAATDataEntity> validate(CaseDetails caseDetailsJson) throws ValidationException {

        final Optional<DefendantMAATDataEntity> defendantViewEntity =
                defendantMAATDataRepository.findBymaatId(caseDetailsJson.getMaatId());

        defendantViewEntity.orElseThrow(() -> new ValidationException("MAAT Defendant details not available."));

        return defendantViewEntity;
    }
}
