package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.applicant.mapper.RepOrderApplicantLinksMapper;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerService {

    private final RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;
    private final RepOrderApplicantLinksMapper repOrderApplicantLinksMapper;
    
    public Integer getPartnerLegacyId(Integer repId) {
        List<RepOrderApplicantLinksDTO> applicantLinks = findPartner(repId);

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

    private List<RepOrderApplicantLinksDTO> findPartner(Integer repId) {
        List<RepOrderApplicantLinksEntity> repOrderApplicantLinksEntities =
            repOrderApplicantLinksRepository.findAllByRepId(repId);

        if (repOrderApplicantLinksEntities.isEmpty()) {
            return null;
        }

        return repOrderApplicantLinksMapper.
            mapEntityToDTO(repOrderApplicantLinksEntities);
    }
}
