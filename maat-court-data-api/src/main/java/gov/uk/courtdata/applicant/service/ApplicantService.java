package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.dto.ApplicantDTO;
import gov.uk.courtdata.applicant.mapper.ApplicantMapper;
import gov.uk.courtdata.applicant.repository.ApplicantRepository;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final ApplicantMapper applicantMapper;

    public ApplicantDTO find(Integer id) {
        log.info("ApplicantService::find - Start");
        Applicant applicantEntity = applicantRepository.findById(id)
                .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("Applicant not found for id %d", id)));
        return applicantMapper.
                mapEntityToDTO(applicantEntity);
    }

    public void update(Integer id, ApplicantDTO applicantDTO) {
        log.info("ApplicantService::update - Start");
        Applicant applicant = applicantRepository.getReferenceById(applicantDTO.getId());
        applicant = applicantMapper.updateApplicantEntity(applicantDTO, applicant);
        applicantRepository.saveAndFlush(applicant);
    }

    public void delete(Integer id) {
        log.info("ApplicantService::delete - Start");
        applicantRepository.deleteById(id);
    }

    public void create(ApplicantDTO applicantDTO) {
        log.info("ApplicantService::create - Start");
        Applicant applicantEntity = applicantMapper.mapDTOToEntity(applicantDTO);
        applicantRepository.saveAndFlush(applicantEntity);
    }

}
