package gov.uk.courtdata.assessment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.assessment.impl.PassportAssessmentImpl;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.dto.AssessorDetails;
import gov.uk.courtdata.entity.PassportAssessmentEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PassportAssessmentServiceTest {
    @Mock
    private PassportAssessmentImpl passportAssessmentImpl;

    @InjectMocks
    private PassportAssessmentService passportAssessmentService;

    @Test
    void
            givenValidPassportAssessmentId_whenFindPassportAssessorDetailsIsInvoked_thenPopulatedAssessorDetailsAreReturned() {
        int passportAssessmentId = 1234;
        final String username = TestEntityDataBuilder.ASSESSOR_USER_NAME;
        PassportAssessmentEntity passportAssessment = PassportAssessmentEntity.builder()
                .userCreated(username)
                .userCreatedEntity(TestEntityDataBuilder.getUserEntity())
                .build();
        when(passportAssessmentImpl.find(passportAssessmentId)).thenReturn(passportAssessment);

        AssessorDetails passportAssessorDetails =
                passportAssessmentService.findPassportAssessorDetails(passportAssessmentId);

        assertThat(passportAssessorDetails.getFullName()).isEqualTo("Karen Greaves");
        assertThat(passportAssessorDetails.getUserName()).isEqualTo(username);
    }
}
