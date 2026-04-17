package gov.uk.courtdata.passport.service;

import gov.uk.courtdata.applicant.service.PartnerService;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import gov.uk.courtdata.passport.mapper.PassportAssessmentEvidenceMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;
import uk.gov.justice.laa.crime.common.model.evidence.ApiIncomeEvidence;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentEvidenceService {

    private final PassportAssessmentPersistenceService passportAssessmentPersistenceService;
    private final PassportAssessmentEvidenceMapper passportAssessmentEvidenceMapper;
    private final PartnerService partnerService;
    
    @Transactional(readOnly = true)
    public ApiGetPassportEvidenceResponse find(int passportAssessmentId) {
        PassportAssessmentEntity passportAssessment = 
            passportAssessmentPersistenceService.find(passportAssessmentId);
        
        Integer partnerLegacyId = 
            partnerService.getPartnerLegacyId(passportAssessment.getRepOrder().getId());

        List<PassportAssessmentEvidenceEntity> allEvidence =
            passportAssessment.getPassportAssessmentEvidences();

        Map<Boolean, List<PassportAssessmentEvidenceEntity>> partitioned =
            allEvidence.stream()
                .collect(Collectors.partitioningBy(
                    evidence -> isPartnerEvidence(
                        evidence,
                        partnerLegacyId)));

        List<ApiIncomeEvidence> partnerEvidence = partitioned.get(true).stream()
            .map(passportAssessmentEvidenceMapper::toApiIncomeEvidence)
            .toList();

        List<ApiIncomeEvidence> applicantEvidence = partitioned.get(false).stream()
            .map(passportAssessmentEvidenceMapper::toApiIncomeEvidence)
            .toList();

        ApiGetPassportEvidenceResponse response =
            passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(passportAssessment);

        response.setPartnerEvidenceItems(partnerEvidence);
        response.setApplicantEvidenceItems(applicantEvidence);
        
        return response;
    }

    private boolean isPartnerEvidence(PassportAssessmentEvidenceEntity evidenceEntity, 
        Integer partnerId) {
        Integer applicantId = evidenceEntity.getApplicant() != null
            ? evidenceEntity.getApplicant().getId()
            : null;
        return applicantId == null || applicantId.equals(partnerId);
    }

}
