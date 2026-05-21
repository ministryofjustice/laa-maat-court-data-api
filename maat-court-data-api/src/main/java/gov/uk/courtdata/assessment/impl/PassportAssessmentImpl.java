package gov.uk.courtdata.assessment.impl;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentImpl {

    private final PassportAssessmentRepository passportAssessmentRepository;

    public PassportAssessmentEntity find(Integer passportAssessmentId) {
        return passportAssessmentRepository.getReferenceById(passportAssessmentId);
    }
}
