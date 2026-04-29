package gov.uk.courtdata.passport.service;

import gov.uk.courtdata.applicant.service.PartnerResolver;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.passport.mapper.PassportAssessmentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.crime.common.model.passported.ApiGetPassportedAssessmentResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentServiceV2 {

    private final PassportAssessmentPersistenceService passportAssessmentPersistenceService;
    private final PassportAssessmentMapper passportAssessmentMapper;
    private final PartnerResolver partnerResolver;
    
    @Transactional(readOnly = true)
    public ApiGetPassportedAssessmentResponse find(int passportAssessmentId) {
        PassportAssessmentEntity passportAssessmentEntity = passportAssessmentPersistenceService.find(passportAssessmentId);

        Integer partnerLegacyId =
            partnerResolver.getPartnerLegacyId(passportAssessmentEntity.getRepOrder().getId());
        
        return passportAssessmentMapper.toApiGetPassportedAssessmentResponse(passportAssessmentEntity,
            partnerLegacyId);
    }
}
