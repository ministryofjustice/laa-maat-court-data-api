package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.assessment.mapper.PassportAssessmentMapper;
import gov.uk.courtdata.dto.PassportAssessmentDTO;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentImpl {

    private final PassportAssessmentMapper assessmentMapper;
    private final PassportAssessmentRepository passportAssessmentRepository;
    private final FinancialAssessmentRepository financialAssessmentRepository;
    private final HardshipReviewRepository hardshipReviewRepository;

    public PassportAssessmentEntity find(Integer passportAssessmentId) {
        return passportAssessmentRepository.getReferenceById(passportAssessmentId);
    }

    public PassportAssessmentEntity findByRepId(int repId) {
        return passportAssessmentRepository.findByRepId(repId).orElse(null);
    }

    public PassportAssessmentEntity update(PassportAssessmentDTO passportAssessmentDTO) {
        PassportAssessmentEntity existingPassportAssessment = passportAssessmentRepository.getReferenceById(passportAssessmentDTO.getId());
        existingPassportAssessment.setNworCode(passportAssessmentDTO.getNworCode());
        existingPassportAssessment.setCmuId(passportAssessmentDTO.getCmuId());
        existingPassportAssessment.setAssessmentDate(passportAssessmentDTO.getAssessmentDate());
        existingPassportAssessment.setPartnerBenefitClaimed(passportAssessmentDTO.getPartnerBenefitClaimed());
        existingPassportAssessment.setPartnerFirstName(passportAssessmentDTO.getPartnerFirstName());
        existingPassportAssessment.setPartnerSurname(passportAssessmentDTO.getPartnerSurname());
        existingPassportAssessment.setPartnerNiNumber(passportAssessmentDTO.getPartnerNiNumber());
        existingPassportAssessment.setPartnerDob(passportAssessmentDTO.getPartnerDob());
        existingPassportAssessment.setIncomeSupport(passportAssessmentDTO.getIncomeSupport());
        existingPassportAssessment.setJobSeekers(passportAssessmentDTO.getJobSeekers());
        existingPassportAssessment.setStatePensionCredit(passportAssessmentDTO.getStatePensionCredit());
        existingPassportAssessment.setPcobConfirmation(passportAssessmentDTO.getPcobConfirmation());
        existingPassportAssessment.setResult(passportAssessmentDTO.getResult());
        existingPassportAssessment.setDwpResult(passportAssessmentDTO.getDwpResult());
        existingPassportAssessment.setPassportNote(passportAssessmentDTO.getPassportNote());
        existingPassportAssessment.setUnder18HeardInYouthCourt(passportAssessmentDTO.getUnder18HeardInYouthCourt());
        existingPassportAssessment.setUnder18HeardInMagsCourt(passportAssessmentDTO.getUnder18HeardInMagsCourt());
        existingPassportAssessment.setLastSignOnDate(passportAssessmentDTO.getLastSignOnDate());
        existingPassportAssessment.setEsa(passportAssessmentDTO.getEsa());
        existingPassportAssessment.setUnder18FullEducation(passportAssessmentDTO.getUnder18FullEducation());
        existingPassportAssessment.setUnder16(passportAssessmentDTO.getUnder16());
        existingPassportAssessment.setBetween16And17(passportAssessmentDTO.getBetween16And17());
        existingPassportAssessment.setPastStatus(passportAssessmentDTO.getPastStatus());
        existingPassportAssessment.setWhoDWPChecked(passportAssessmentDTO.getWhoDWPChecked());
        existingPassportAssessment.setDateCompleted(passportAssessmentDTO.getDateCompleted());
        existingPassportAssessment.setUserModified(passportAssessmentDTO.getUserModified());
        existingPassportAssessment.setDateModified(passportAssessmentDTO.getDateModified() != null ? passportAssessmentDTO.getDateModified() : LocalDateTime.now());
        return passportAssessmentRepository.save(existingPassportAssessment);
    }

    public void delete(Integer passportAssessmentId) {
        passportAssessmentRepository.deleteById(passportAssessmentId);
    }

    public PassportAssessmentEntity create(PassportAssessmentDTO passportAssessmentDTO) {
        PassportAssessmentEntity passportAssessmentEntity = assessmentMapper.passportAssessmentDtoToPassportAssessmentEntity(passportAssessmentDTO);
        return passportAssessmentRepository.save(passportAssessmentEntity);
    }

    public void setOldPassportAssessmentAsReplaced(PassportAssessmentEntity passportAssessment, Integer financialAssessmentId) {
        passportAssessmentRepository.updatePreviousPassportAssessmentsAsReplaced(
                passportAssessment.getRepOrder().getId(), passportAssessment.getId()
        );
        financialAssessmentRepository.updateAllPreviousFinancialAssessmentsAsReplaced(
                passportAssessment.getRepOrder().getId()
        );
        hardshipReviewRepository.replaceOldHardshipReviews(
                passportAssessment.getRepOrder().getId(), financialAssessmentId
        );
    }
}
