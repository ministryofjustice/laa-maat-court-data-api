package gov.uk.courtdata.validator;

import gov.uk.courtdata.dto.CreateLinkDto;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
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

    private final DefendantValidator defendantValidator;

    private final SolicitorValidator solicitorValidator;

    private final CourtValidator courtValidator;

    private final ReferenceDataValidator referenceDataValidator;


    /**
     * @param caseDetails
     */
    public CreateLinkDto validate(CaseDetails caseDetails) {


        maatIdValidator.validate(caseDetails.getMaatId());
        linkExistsValidator.validate(caseDetails);
        courtValidator.validate(caseDetails);
        referenceDataValidator.validate(caseDetails);
        final Optional<SolicitorMAATDataEntity> solicitorMAATDataEntity
                = solicitorValidator.validate(caseDetails);
        final Optional<DefendantMAATDataEntity> defendantMAATDataEntity
                = defendantValidator.validate(caseDetails.getMaatId());


        return CreateLinkDto.builder().caseDetails(caseDetails)
                .solicitorMAATDataEntity(solicitorMAATDataEntity.get())
                .defendantMAATDataEntity(defendantMAATDataEntity.get()).build();
    }

}
