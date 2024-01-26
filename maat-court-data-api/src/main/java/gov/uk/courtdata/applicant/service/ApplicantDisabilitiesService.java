package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.dto.ApplicantDisabilitiesDTO;
import gov.uk.courtdata.applicant.entity.ApplicantDisabilitiesEntity;
import gov.uk.courtdata.applicant.entity.ApplicantHistoryDisabilitiesEntity;
import gov.uk.courtdata.applicant.mapper.ApplicantDisabilitiesMapper;
import gov.uk.courtdata.applicant.repository.ApplicantDisabilitiesRepository;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantDisabilitiesService {
    private final ApplicantDisabilitiesMapper applicantDisabilitiesMapper;
    private final ApplicantDisabilitiesRepository applicantDisabilitiesRepository;

    @Transactional(readOnly = true)
    public ApplicantDisabilitiesDTO find(Integer id) {
        log.info("ApplicantDisabilitiesService::find - Start");
        ApplicantDisabilitiesEntity applicantDisabilitiesEntity = getApplicantDisabilitiesEntity(id);
        return applicantDisabilitiesMapper.
                mapEntityToDTO(applicantDisabilitiesEntity);
    }

    @Transactional
    public ApplicantDisabilitiesDTO create(ApplicantDisabilitiesDTO applicantDisabilitiesDTO) {
        log.info("ApplicantDisabilitiesService::create - Start");
        ApplicantDisabilitiesEntity applicantDisabilitiesEntity = applicantDisabilitiesMapper.
                mapDTOToApplicantDisabilitiesEntity(applicantDisabilitiesDTO);
        addApplicantHistoryDisabilities(applicantDisabilitiesDTO, applicantDisabilitiesEntity);
        return applicantDisabilitiesMapper.
                mapEntityToDTO(applicantDisabilitiesRepository.save(applicantDisabilitiesEntity));
    }

    @Transactional
    public ApplicantDisabilitiesDTO update(ApplicantDisabilitiesDTO applicantDisabilitiesDTO) {
        log.info("ApplicantDisabilitiesService::update - Start");
        Integer id = applicantDisabilitiesDTO.getId();
        ApplicantDisabilitiesEntity applicantDisabilitiesEntity =
                getApplicantDisabilitiesEntity(id);
        applicantDisabilitiesMapper.
                updateApplicantDisabilitiesDTOToApplicantDisabilitiesEntity(
                        applicantDisabilitiesDTO,applicantDisabilitiesEntity);
        addApplicantHistoryDisabilities(applicantDisabilitiesDTO, applicantDisabilitiesEntity);
        return applicantDisabilitiesMapper.
                mapEntityToDTO(applicantDisabilitiesRepository.save(applicantDisabilitiesEntity));
    }

    @Transactional
    public void delete(Integer id) {
        log.info("ApplicantDisabilitiesService::delete - Start");
        ApplicantDisabilitiesEntity applicantDisabilitiesEntity =
                getApplicantDisabilitiesEntity(id);
        applicantDisabilitiesRepository.delete(applicantDisabilitiesEntity);
    }

    private ApplicantDisabilitiesEntity getApplicantDisabilitiesEntity(Integer id) {
        ApplicantDisabilitiesEntity applicantDisabilitiesEntity =
                applicantDisabilitiesRepository.findById(id).orElse(null);
        if (applicantDisabilitiesEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("Applicant Disability details not found for id %d", id));
        }
        return applicantDisabilitiesEntity;
    }

    private ApplicantHistoryDisabilitiesEntity getApplicantHistoryDisabilitiesEntity(ApplicantDisabilitiesDTO applicantDisabilitiesDTO) {
        ApplicantHistoryDisabilitiesEntity applicantHistoryDisabilitiesEntity = applicantDisabilitiesMapper
                .mapDTOToApplicantHistoryDisabilitiesEntity(applicantDisabilitiesDTO);
        return applicantHistoryDisabilitiesEntity;
    }


    private void addApplicantHistoryDisabilities(ApplicantDisabilitiesDTO applicantDisabilitiesDTO, ApplicantDisabilitiesEntity applicantDisabilitiesEntity) {
        ApplicantHistoryDisabilitiesEntity applicantHistoryDisabilities =
                getApplicantHistoryDisabilitiesEntity(applicantDisabilitiesDTO);
        applicantDisabilitiesEntity.getApplicantHistoryDisabilityEntities().add(applicantHistoryDisabilities);
    }
}
