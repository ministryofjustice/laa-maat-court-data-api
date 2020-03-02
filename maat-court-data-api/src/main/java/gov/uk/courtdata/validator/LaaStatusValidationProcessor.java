package gov.uk.courtdata.validator;

import gov.uk.courtdata.dto.CourtDataDTO;
import gov.uk.courtdata.entity.DefendantMAATDataEntity;
import gov.uk.courtdata.entity.SolicitorMAATDataEntity;
import gov.uk.courtdata.model.CaseDetails;
import gov.uk.courtdata.model.MessageCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LaaStatusValidationProcessor {

    private final MaatIdValidator maatIdValidator;
    private final LinkRegisterValidator linkRegisterValidator;
    private final SolicitorValidator solicitorValidator;
    private final DefendantValidator defendantValidator;


    public CourtDataDTO validate(CaseDetails caseDetails) {

        MessageCollection messageCollection = MessageCollection.builder().build();
        Integer maatId = caseDetails.getMaatId();
        maatIdValidator.validate(maatId);
        // TODO Validate CJS + Libra ID Combination here.
        linkRegisterValidator.validateMAATId(maatId);
        Optional<SolicitorMAATDataEntity> solicitorMAATDataEntity = solicitorValidator.validate(caseDetails);
        Optional<DefendantMAATDataEntity> defendantMAATDataEntity = defendantValidator.validate(maatId);


        return CourtDataDTO.builder()
                .solicitorMAATDataEntity(solicitorMAATDataEntity.get())
                .defendantMAATDataEntity(defendantMAATDataEntity.get())
                .messageCollection(messageCollection)
                .build();
    }
}
