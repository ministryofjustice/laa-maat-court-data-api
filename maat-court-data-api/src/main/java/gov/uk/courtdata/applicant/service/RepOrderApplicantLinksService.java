package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.applicant.mapper.RepOrderApplicantLinksMapper;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
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
    public List<RepOrderApplicantLinksDTO> find(Integer repId) {
        log.info("RepOrderApplicantLinksService::find - Start");
        List<RepOrderApplicantLinksEntity> repOrderApplicantLinksEntities = repOrderApplicantLinksRepository.findAllByRepId(repId);
        if (repOrderApplicantLinksEntities.isEmpty()) {
            throw new RequestedObjectNotFoundException(String.format("Rep Order Applicant Links not found for repId: %d", repId));
        }
        return repOrderApplicantLinksMapper.
                mapEntityToDTO(repOrderApplicantLinksEntities);
    }

    @Transactional
    public RepOrderApplicantLinksDTO update(RepOrderApplicantLinksDTO repOrderApplicantLinksDTO) {
        log.info("RepOrderApplicantLinksService::update - Start");
        Integer id = repOrderApplicantLinksDTO.getId();
        RepOrderApplicantLinksEntity repOrderApplicantLinksEntity = repOrderApplicantLinksRepository.findById(id).orElse(null);
        if (repOrderApplicantLinksEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("Rep Order Applicant Link not found for id %d", id));
        }
        repOrderApplicantLinksMapper.updateRepOrderApplicantLinksDTOToRepOrderApplicantLinksEntity(repOrderApplicantLinksDTO, repOrderApplicantLinksEntity);
        return repOrderApplicantLinksMapper.mapEntityToDTO(repOrderApplicantLinksRepository.saveAndFlush(repOrderApplicantLinksEntity));
    }

}
