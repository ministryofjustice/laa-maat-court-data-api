package gov.uk.courtdata.link.validator;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.validator.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * <code>ValidationProcessor</code> chain of validations on the create link process.
 */
@Slf4j
@AllArgsConstructor
@Component
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

        final Optional<SolicitorMAATDataEntity> solicitorMAATDataEntity
                = solicitorValidator.validate(maatId);
        final Optional<DefendantMAATDataEntity> defendantMAATDataEntity
                = defendantValidator.validate(maatId);


        return CourtDataDTO.builder().caseDetails(caseDetails)
                .solicitorMAATDataEntity(solicitorMAATDataEntity.get())
                .defendantMAATDataEntity(defendantMAATDataEntity.get()).build();
    }

}
