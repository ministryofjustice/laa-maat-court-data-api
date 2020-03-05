package gov.uk.courtdata.validator;

import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

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

        solicitorViewEntity.orElseThrow(
                () -> new ValidationException(format("Solicitor not found for maatId %s",
                        caseDetailsJson.getMaatId())));


        Optional.ofNullable(solicitorViewEntity.get().getAccountCode()).filter(StringUtils::isNotBlank)
                .orElseThrow(() -> new ValidationException(format("Solicitor account code not available for maatId %s.", caseDetailsJson.getMaatId())));

        return solicitorViewEntity;
    }

}
