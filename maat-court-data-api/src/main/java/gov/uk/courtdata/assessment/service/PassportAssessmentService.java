package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.PassportAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import gov.uk.courtdata.util.UserEntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentService {
    public static final String STATUS_COMPLETE = "COMPLETE";
    private final PassportAssessmentImpl passportAssessmentImpl;
    private final PassportAssessmentMapper passportAssessmentMapper;

    @Transactional(readOnly = true)
    public PassportAssessmentDTO find(Integer passportAssessmentId) {
        PassportAssessmentEntity passportAssessmentEntity = passportAssessmentImpl.find(passportAssessmentId);
        if (passportAssessmentEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No Passport Assessment found for ID: %s", passportAssessmentId));
        }
        return buildPassportAssessmentDTO(passportAssessmentEntity);
    }

    @Transactional(readOnly = true)
    public PassportAssessmentDTO findByRepId(int repId) {
        PassportAssessmentEntity passportAssessmentEntity = passportAssessmentImpl.findByRepId(repId);
        if (passportAssessmentEntity == null) {
            throw new RequestedObjectNotFoundException(String.format("No Passport Assessment found for REP ID: %s", repId));
        }
        return buildPassportAssessmentDTO(passportAssessmentEntity);
    }

    @Transactional
    public PassportAssessmentDTO update(UpdatePassportAssessment updatePassportAssessment) {
        log.info("Update Passport Assessment - Transaction Processing - Start");
        PassportAssessmentDTO passportAssessmentDTO =
                passportAssessmentMapper.updatePassportAssessmentToPassportAssessmentDTO(updatePassportAssessment);
        log.info("Updating existing passport assessment record");
        PassportAssessmentEntity existingPassportAssessmentEntity = passportAssessmentImpl.find(passportAssessmentDTO.getId());
        if (existingPassportAssessmentEntity.getPastStatus().equals(STATUS_COMPLETE)) {
            throw new ValidationException("User cannot modify a completed assessment");
        }
        PassportAssessmentEntity passportAssessmentEntity = passportAssessmentImpl.update(passportAssessmentDTO);
        log.info("Update Passport Assessment - Transaction Processing - End");
        return buildPassportAssessmentDTO(passportAssessmentEntity);
    }

    public void delete(Integer passportAssessmentId) {
        passportAssessmentImpl.delete(passportAssessmentId);
    }

    @Transactional
    public PassportAssessmentDTO create(CreatePassportAssessment createPassportAssessment) {
        log.info("Create Passport Assessment - Transaction Processing - Start");
        PassportAssessmentDTO passportAssessmentDTO =
                passportAssessmentMapper.createPassportAssessmentToPassportAssessmentDTO(createPassportAssessment);
        log.info("Creating new passport assessment record");
        PassportAssessmentEntity assessmentEntity = passportAssessmentImpl.create(passportAssessmentDTO);
        log.info("Setting outdated records as replaced");
        passportAssessmentImpl.setOldPassportAssessmentAsReplaced(
                assessmentEntity, createPassportAssessment.getFinancialAssessmentId()
        );
        log.info("Create Passport Assessment - Transaction Processing - End");
        return buildPassportAssessmentDTO(assessmentEntity);
    }

    PassportAssessmentDTO buildPassportAssessmentDTO(PassportAssessmentEntity assessmentEntity) {
        return passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(assessmentEntity);
    }

    public AssessorDetails findPassportAssessorDetails(int passportAssessmentId) {
        PassportAssessmentEntity passportAssessmentEntity = passportAssessmentImpl.find(passportAssessmentId);
        if (passportAssessmentEntity == null) {
            String message = String.format("No Passport Assessment found for passport assessment Id: [%s]", passportAssessmentId);
            throw new RequestedObjectNotFoundException(message);
        }

        return AssessorDetails.builder()
                .fullName(UserEntityUtils.extractFullName(passportAssessmentEntity.getUserCreatedEntity()))
                .userName(passportAssessmentEntity.getUserCreated())
                .build();
    }
}
