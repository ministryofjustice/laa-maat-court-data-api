package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.applicant.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;
import gov.uk.courtdata.applicant.mapper.ApplicantHistoryMapper;
import gov.uk.courtdata.applicant.repository.ApplicantHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantHistoryService {

    private final ApplicantHistoryMapper applicantHistoryMapper;
    private final ApplicantHistoryRepository applicantHistoryRepository;

    @Transactional
    public ApplicantHistoryDTO update(ApplicantHistoryDTO applicantHistoryDTO) {
        log.info("ApplicantHistoryService::update - Start");
        Integer id = applicantHistoryDTO.getId();
        ApplicantHistoryEntity applicantHistoryEntity = applicantHistoryRepository.findById(id).orElse(null);
        if (applicantHistoryEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("Applicant History not found for id %d", id));
        }
        applicantHistoryMapper.updateApplicantHistoryDTOToApplicantHistoryEntity(applicantHistoryDTO, applicantHistoryEntity);
        return applicantHistoryMapper.mapEntityToDTO(applicantHistoryRepository.saveAndFlush(applicantHistoryEntity));
    }
}