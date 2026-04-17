package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartnerResolver {

    private final RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;

    public Integer getPartnerLegacyId(Integer repId) {
        return repOrderApplicantLinksRepository
            .findFirstByRepIdAndLinkDateIsNotNullAndUnlinkDateIsNull(repId)
            .map(RepOrderApplicantLinksEntity::getPartnerApplId)
            .orElse(null);
    }
}
