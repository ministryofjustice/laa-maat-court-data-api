package gov.uk.courtdata.preupdatechecks.service;

import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.preupdatechecks.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.preupdatechecks.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.preupdatechecks.mapper.RepOrderApplicantLinksMapper;
import gov.uk.courtdata.preupdatechecks.repository.RepOrderApplicantLinksRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepOrderApplicantLinksService {

    private final RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;
    private final RepOrderApplicantLinksMapper repOrderApplicantLinksMapper;

    @Transactional(readOnly = true)
    public List<RepOrderApplicantLinksDTO> getRepOrderApplicantLinks(Integer repId) {
        log.info("RepOrderApplicantLinksService - findAllByRepId - Start");
        List<RepOrderApplicantLinksEntity> repOrderApplicantLinksEntities = repOrderApplicantLinksRepository.findAllByRepId(repId);
        if (repOrderApplicantLinksEntities.isEmpty()) {
            throw new RequestedObjectNotFoundException(String.format("Rep Order Applicant Links not found for repId: %d", repId));
        }
        return repOrderApplicantLinksMapper.
                repOrderApplicantLinksEntityToRepOrderApplicantLinksDTO(repOrderApplicantLinksEntities);
    }

}
