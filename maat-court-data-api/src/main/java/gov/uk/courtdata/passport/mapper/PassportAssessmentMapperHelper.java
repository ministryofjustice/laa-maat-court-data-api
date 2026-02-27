package gov.uk.courtdata.passport.mapper;

import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.applicant.mapper.RepOrderApplicantLinksMapper;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PassportAssessmentMapperHelper {
    
    private final RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;
    private final RepOrderApplicantLinksMapper repOrderApplicantLinksMapper;
    
    @Named("partnerLegacyIdMapper")
    public Integer mapPartnerLegacyId(PassportAssessmentEntity passportAssessmentEntity) {
        List<RepOrderApplicantLinksDTO> applicantLinks = findPartner(passportAssessmentEntity);

        if (applicantLinks == null) {
            return null;
        }
        
        return applicantLinks.stream()
            .filter(repOrderApplicantLink ->
                repOrderApplicantLink.getUnlinkDate() == null &&
                repOrderApplicantLink.getLinkDate() != null)
            .map(RepOrderApplicantLinksDTO::getPartnerApplId)
            .findFirst()
            .orElse(null);
    }

    public List<RepOrderApplicantLinksDTO> findPartner(PassportAssessmentEntity passportAssessmentEntity) {
        List<RepOrderApplicantLinksEntity> repOrderApplicantLinksEntities = 
            repOrderApplicantLinksRepository.findAllByRepId(passportAssessmentEntity.getRepOrder().getId());
        
        if (repOrderApplicantLinksEntities.isEmpty()) {
            return null;
        }

        return repOrderApplicantLinksMapper.
            mapEntityToDTO(repOrderApplicantLinksEntities);
    }
}
