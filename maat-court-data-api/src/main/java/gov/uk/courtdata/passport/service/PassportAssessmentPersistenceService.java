package gov.uk.courtdata.passport.service;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentPersistenceService {
    
    private final PassportAssessmentRepository passportAssessmentRepository;
    
    public PassportAssessmentEntity find(Integer passportAssessmentId) {
        return passportAssessmentRepository.findById(passportAssessmentId)
            .orElseThrow(() -> new RequestedObjectNotFoundException(
                String.format("No Passported Assessment found for ID: %s", passportAssessmentId)));
    }
}
