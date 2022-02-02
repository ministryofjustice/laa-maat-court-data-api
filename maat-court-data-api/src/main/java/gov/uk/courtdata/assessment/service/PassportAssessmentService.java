package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.PassportAssessmentImpl;
import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.MAATCourtDataException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.CreatePassportAssessment;
import gov.uk.courtdata.model.assessment.UpdatePassportAssessment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassportAssessmentService {
    public static final String STATUS_COMPLETE = "COMPLETE";
    private final PassportAssessmentImpl passportAssessmentImpl;
    private final PassportAssessmentMapper passportAssessmentMapper;

    public PassportAssessmentDTO find(Integer passportAssessmentId) {
        PassportAssessmentEntity passportAssessmentEntity = passportAssessmentImpl.find(passportAssessmentId);
        return buildPassportAssessmentDTO(passportAssessmentEntity);
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public PassportAssessmentDTO update(UpdatePassportAssessment updatePassportAssessment) {
        log.info("Update Passport Assessment - Transaction Processing - Start");
        PassportAssessmentDTO passportAssessmentDTO =
                passportAssessmentMapper.updatePassportAssessmentToPassportAssessmentDTO(updatePassportAssessment);
        log.info("Updating existing passport assessment record");
        PassportAssessmentEntity existingPassportAssessmentEntity = passportAssessmentImpl.find(passportAssessmentDTO.getId());
        if(existingPassportAssessmentEntity.getPastStatus().equals(STATUS_COMPLETE)) {
            throw new ValidationException("User cannot modify a completed assessment");
        }
        PassportAssessmentEntity passportAssessmentEntity = passportAssessmentImpl.update(passportAssessmentDTO);
        log.info("Update Passport Assessment - Transaction Processing - End");
        return buildPassportAssessmentDTO(passportAssessmentEntity);
    }

    public void delete(Integer passportAssessmentId) {
        passportAssessmentImpl.delete(passportAssessmentId);
    }

    @Transactional(rollbackFor = MAATCourtDataException.class)
    public PassportAssessmentDTO create(CreatePassportAssessment createPassportAssessment) {
        log.info("Create Passport Assessment - Transaction Processing - Start");
        PassportAssessmentDTO passportAssessmentDTO =
                passportAssessmentMapper.createPassportAssessmentToPassportAssessmentDTO(createPassportAssessment);
        log.info("Creating new passport assessment record");
        PassportAssessmentEntity assessmentEntity = passportAssessmentImpl.create(passportAssessmentDTO);
        log.info("Setting outdated records as replaced");
        passportAssessmentImpl.setOldPassportAssessmentAsReplaced(passportAssessmentDTO);
        log.info("Create Passport Assessment - Transaction Processing - End");
        return buildPassportAssessmentDTO(assessmentEntity);
    }

    public PassportAssessmentDTO buildPassportAssessmentDTO(PassportAssessmentEntity assessmentEntity) {
        PassportAssessmentDTO passportAssessmentDTO = passportAssessmentMapper.passportAssessmentEntityToPassportAssessmentDTO(assessmentEntity);
        return passportAssessmentDTO;
    }

}
