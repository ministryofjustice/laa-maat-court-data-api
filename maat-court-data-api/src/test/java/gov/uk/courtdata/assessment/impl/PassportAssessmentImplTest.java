package gov.uk.courtdata.assessment.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.repository.PassportAssessmentRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PassportAssessmentImplTest {

    @Spy
    @InjectMocks
    private PassportAssessmentImpl passportAssessmentImpl;

    @Spy
    private PassportAssessmentRepository passportAssessmentRepository;

    private static final int MOCK_ASSESSMENT_ID = 1000;

    @Test
    void whenFindIsInvoked_thenAssessmentIsRetrieved() {
        when(passportAssessmentRepository.getReferenceById(any()))
                .thenReturn(PassportAssessmentEntity.builder()
                        .id(MOCK_ASSESSMENT_ID)
                        .build());
        PassportAssessmentEntity returned = passportAssessmentImpl.find(MOCK_ASSESSMENT_ID);
        assertThat(returned.getId()).isEqualTo(MOCK_ASSESSMENT_ID);
    }
}
