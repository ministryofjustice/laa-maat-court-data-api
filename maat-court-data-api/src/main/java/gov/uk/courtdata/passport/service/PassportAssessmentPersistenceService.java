package gov.uk.courtdata.passport.service;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentPersistenceService {
    
    private final PassportAssessmentRepository passportAssessmentRepository;
    private final HardshipReviewRepository hardshipReviewRepository;
    private final FinancialAssessmentRepository financialAssessmentRepository;
    
    public PassportAssessmentEntity find(Integer passportAssessmentId) {
        return passportAssessmentRepository.findById(passportAssessmentId)
            .orElseThrow(() -> new RequestedObjectNotFoundException(
                String.format("No Passported Assessment found for ID: %s", passportAssessmentId)));
    }

    @Transactional
    public PassportAssessmentEntity create(PassportAssessmentEntity passportAssessmentEntity) {
        save(passportAssessmentEntity);
        invalidateOldData(passportAssessmentEntity);
        return passportAssessmentEntity;
    }

    public PassportAssessmentEntity save(PassportAssessmentEntity passportAssessmentEntity) {
        return passportAssessmentRepository.save(passportAssessmentEntity);
    }

    private void invalidateOldData(PassportAssessmentEntity passportAssessmentEntity) {
        passportAssessmentRepository.updatePreviousPassportAssessmentsAsReplaced(
                passportAssessmentEntity.getRepOrder().getId(), passportAssessmentEntity.getId()
        );
        // todo This needs to keep one? But how to identify?
        financialAssessmentRepository.updateAllPreviousFinancialAssessmentsAsReplaced(
                passportAssessmentEntity.getRepOrder().getId()
        );
        hardshipReviewRepository.replaceOldHardshipReviews(
                passportAssessmentEntity.getRepOrder().getId()
        );
    }
}
