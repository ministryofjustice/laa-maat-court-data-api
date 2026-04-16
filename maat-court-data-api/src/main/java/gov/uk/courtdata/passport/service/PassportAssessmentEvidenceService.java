package gov.uk.courtdata.passport.service;

import gov.uk.courtdata.applicant.service.PartnerService;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.passport.mapper.PassportAssessmentEvidenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentEvidenceService {

    private final PassportAssessmentPersistenceService passportAssessmentPersistenceService;
    private final PassportAssessmentEvidenceMapper passportAssessmentEvidenceMapper;
    private final PartnerService partnerService;
    
    @Transactional(readOnly = true)
    public ApiGetPassportEvidenceResponse find(int passportAssessmentId) {
        PassportAssessmentEntity passportAssessmentEntity = 
            passportAssessmentPersistenceService.find(passportAssessmentId);
        
        Integer partnerLegacyId = 
            partnerService.getPartnerLegacyId(passportAssessmentEntity.getRepOrder().getId());
        
        return passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(
            passportAssessmentEntity, partnerLegacyId);
    }
}
