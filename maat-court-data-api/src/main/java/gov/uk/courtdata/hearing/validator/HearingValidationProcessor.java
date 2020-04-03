package gov.uk.courtdata.hearing.validator;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.validator.DefendantValidator;
import gov.uk.courtdata.validator.LinkRegisterValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import gov.uk.courtdata.validator.SolicitorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HearingValidationProcessor {

    private final LinkRegisterValidator linkRegisterValidator;
    private final MaatIdValidator maatIdValidator;
    private final DefendantValidator defendantValidator;
    private final SolicitorValidator solicitorValidator;

    public CourtDataDTO validate(CaseDetails caseDetails) {

        Integer maatId = caseDetails.getMaatId();
        maatIdValidator.validate(maatId);
        linkRegisterValidator.validate(maatId);
        //TODO - Implement a Mechanism to validate Duplicate Messages
        final Optional<SolicitorMAATDataEntity> solicitorMAATDataEntity
                = solicitorValidator.validate(caseDetails);
        final Optional<DefendantMAATDataEntity> defendantMAATDataEntity
                = defendantValidator.validate(caseDetails.getMaatId());
        return CourtDataDTO.builder().caseDetails(caseDetails)
                .solicitorMAATDataEntity(solicitorMAATDataEntity.get())
                .defendantMAATDataEntity(defendantMAATDataEntity.get()).build();
    }

}
