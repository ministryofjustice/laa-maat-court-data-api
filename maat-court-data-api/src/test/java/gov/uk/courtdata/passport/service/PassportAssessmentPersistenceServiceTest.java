package gov.uk.courtdata.passport.service;

import static gov.uk.courtdata.builder.TestModelDataBuilder.PASSPORT_ASSESSMENT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PassportAssessmentPersistenceServiceTest {

    @Mock
    private PassportAssessmentRepository passportAssessmentRepository;
    
    @InjectMocks
    private PassportAssessmentPersistenceService passportAssessmentPersistenceService;
    
    @Test
    void givenAPassportAssessmentId_whenFindIsInvoked_thenPassportAssessmentIsRetrieved() {
        PassportAssessmentEntity passportAssessmentEntity = PassportAssessmentEntity.builder()
            .id(PASSPORT_ASSESSMENT_ID)
            .build();
        
        when(passportAssessmentRepository.findById(any())).thenReturn(Optional.ofNullable(passportAssessmentEntity));
        var passportAssessment = passportAssessmentPersistenceService.find(PASSPORT_ASSESSMENT_ID);
        assertEquals(PASSPORT_ASSESSMENT_ID, passportAssessment.getId());
    }

    @Test
    void givenAnInvalidPassportAssessmentId_whenFindIsInvoked_thenExceptionIsThrown() {
        when(passportAssessmentRepository.findById(any())).thenReturn(Optional.empty());

        assertThatExceptionOfType(RequestedObjectNotFoundException.class)
            .isThrownBy(() -> passportAssessmentPersistenceService.find(PASSPORT_ASSESSMENT_ID))
            .withMessageContaining(String.format("No Passported Assessment found for ID: %d", PASSPORT_ASSESSMENT_ID));
    }
}
