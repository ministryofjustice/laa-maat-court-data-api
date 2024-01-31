package gov.uk.courtdata.applicant.service;

import gov.uk.courtdata.applicant.dto.ApplicantDTO;
import gov.uk.courtdata.applicant.mapper.ApplicantMapper;
import gov.uk.courtdata.applicant.repository.ApplicantRepository;
import gov.uk.courtdata.eform.exception.UsnException;
import gov.uk.courtdata.eform.repository.entity.EformsDecisionHistory;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;


import java.lang.reflect.Field;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final ApplicantMapper applicantMapper;

    private static final String APPLICANT_NOT_FOUND = "The APPLICANT [%d] not found in APPLICANTS table";

    public ApplicantDTO find(Integer id) {
        log.info("ApplicantService::find - Start");
        Applicant applicantEntity = applicantRepository.findById(id)
                .orElseThrow(() -> new RequestedObjectNotFoundException(String.format("Applicant not found for id %d", id)));
        return applicantMapper.
                mapEntityToDTO(applicantEntity);
    }

    public void update(Integer id, Applicant applicant) {
        log.info("ApplicantService::update - Start");
        Applicant currentApplicant = applicantRepository.getById(id);

        if (currentApplicant != null) {
            for (Field declaredField : Applicant.class.getDeclaredFields()) {
                ReflectionUtils.makeAccessible(declaredField);
                Object fieldValue = ReflectionUtils.getField(declaredField, applicant);
                if (fieldValue != null) {
                    ReflectionUtils.setField(declaredField, currentApplicant, fieldValue);
                }
            };
            applicantRepository.save(currentApplicant);
        } else {
            throw new RequestedObjectNotFoundException("Applicant not found for id " + id);
        }
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
