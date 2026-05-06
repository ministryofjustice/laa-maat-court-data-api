package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.entity.FinancialAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssessmentReplacementService {
    private final PassportAssessmentRepository passportAssessmentRepository;
    private final FinancialAssessmentRepository financialAssessmentRepository;
    private final HardshipReviewRepository hardshipReviewRepository;

    @Transactional
    public void replacePreviousAssessments(PassportAssessmentEntity passportAssessment) {
        if(passportAssessment.getRepOrder() != null
                && passportAssessment.getRepOrder().getId() != null
                && passportAssessment.getId() != null){
            int repOrderId = passportAssessment.getRepOrder().getId();
            financialAssessmentRepository.replaceAllByRepId(repOrderId);
            hardshipReviewRepository.replaceAllByRepId(repOrderId);
            passportAssessmentRepository.replaceAllByRepIdExcludingPassportedAssessment(repOrderId, passportAssessment.getId());
        }
    }

    @Transactional
    public void replacePreviousAssessments(FinancialAssessmentEntity financialAssessment){
        if(financialAssessment.getRepOrder() != null
                && financialAssessment.getRepOrder().getId() != null
                && financialAssessment.getId() != null){
            financialAssessmentRepository.replaceAllByRepIdExcludingFinancialAssessment(
                    financialAssessment.getRepOrder().getId(), financialAssessment.getId());
            passportAssessmentRepository.replaceAllByRepId(
                    financialAssessment.getRepOrder().getId());
            hardshipReviewRepository.replaceAllByRepIdExcludingFinancialAssessment(
                    financialAssessment.getRepOrder().getId(), financialAssessment.getId()
            );
        }
    }
}
