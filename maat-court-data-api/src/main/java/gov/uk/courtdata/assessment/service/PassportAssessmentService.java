package gov.uk.courtdata.assessment.service;

import gov.uk.courtdata.assessment.impl.PassportAssessmentImpl;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.util.UserEntityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassportAssessmentService {
    private final PassportAssessmentImpl passportAssessmentImpl;

    public AssessorDetails findPassportAssessorDetails(int passportAssessmentId) {
        PassportAssessmentEntity passportAssessmentEntity = passportAssessmentImpl.find(passportAssessmentId);
        if (passportAssessmentEntity == null) {
            String message = String.format(
                    "No Passport Assessment found for passport assessment Id: [%s]", passportAssessmentId);
            throw new RequestedObjectNotFoundException(message);
        }

        return AssessorDetails.builder()
                .fullName(UserEntityUtils.extractFullName(passportAssessmentEntity.getUserCreatedEntity()))
                .userName(passportAssessmentEntity.getUserCreated())
                .build();
    }
}
