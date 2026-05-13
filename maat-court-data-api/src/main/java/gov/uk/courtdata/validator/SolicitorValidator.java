package gov.uk.courtdata.validator;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.repository.SolicitorMAATDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SolicitorValidator implements IValidator<SolicitorMAATDataEntity, Integer> {

    private final SolicitorMAATDataRepository solicitorMAATDataRepository;

    /**
     * @param maatId
     * @return
     */
    @Override
    public Optional<SolicitorMAATDataEntity> validate(Integer maatId) {

        Optional<SolicitorMAATDataEntity> solicitorViewEntity = solicitorMAATDataRepository.findBymaatId(maatId);

        SolicitorMAATDataEntity solicitorData = solicitorViewEntity.orElseThrow(
                () -> new ValidationException(format("Solicitor not found for maatId %s", maatId)));

        if (isBlank(solicitorData.getAccountCode()))
            throw new ValidationException(format("Solicitor account code not available for maatId %s.", maatId));

        return solicitorViewEntity;
    }
}
