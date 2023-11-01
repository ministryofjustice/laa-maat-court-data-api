package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.validator.DefendantValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import gov.uk.courtdata.validator.ReferenceDataValidator;
import gov.uk.courtdata.validator.SolicitorValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <code>ValidationProcessor</code> chain of validations on the create link process.
 */
@Slf4j
@Component
@AllArgsConstructor
public class ValidationProcessor {


    private final MaatIdValidator maatIdValidator;

    private final LinkExistsValidator linkExistsValidator;

    private final CPDataValidator cpDataValidator;

    private final DefendantValidator defendantValidator;

    private final SolicitorValidator solicitorValidator;

    private final CourtValidator courtValidator;

    private final ReferenceDataValidator referenceDataValidator;


    /**
     * @param caseDetails
     */
    public CourtDataDTO validate(CaseDetails caseDetails) {


        final Integer maatId = caseDetails.getMaatId();
        maatIdValidator.validate(maatId);
        cpDataValidator.validate(caseDetails);
        linkExistsValidator.validate(maatId);
        courtValidator.validate(caseDetails);
        referenceDataValidator.validate(caseDetails);

        final Optional<SolicitorMAATDataEntity> optSolicitorMAATDataEntity
                = solicitorValidator.validate(maatId);
        SolicitorMAATDataEntity solicitorMAATDataEntity = optSolicitorMAATDataEntity.orElse(null);

        final Optional<DefendantMAATDataEntity> optDefendantMAATDataEntity
                = defendantValidator.validate(maatId);

        DefendantMAATDataEntity defendantMAATDataEntity = optDefendantMAATDataEntity.orElse(null);


        return CourtDataDTO.builder().caseDetails(caseDetails)
                .solicitorMAATDataEntity(solicitorMAATDataEntity)
                .defendantMAATDataEntity(defendantMAATDataEntity).build();
    }

}
