package gov.uk.courtdata.passport.service;

import static gov.uk.courtdata.builder.TestModelDataBuilder.PASSPORT_ASSESSMENT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.FinancialAssessmentRepository;
import gov.uk.courtdata.repository.HardshipReviewRepository;
import gov.uk.courtdata.repository.PassportAssessmentRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PassportAssessmentPersistenceServiceTest {

    @Mock
    private PassportAssessmentRepository passportAssessmentRepository;
    @Mock
    private HardshipReviewRepository hardshipReviewRepository;
    @Mock
    private FinancialAssessmentRepository financialAssessmentRepository;
    
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

    @Test
    void givenCreateCalled_whenCreateIsInvoked_thenRepositoryCallsAreMade(){
        when(passportAssessmentRepository.save(any())).thenReturn(TestEntityDataBuilder.getPassportAssessmentEntity());
        doNothing().when(passportAssessmentRepository).replaceAllByRepIdExcludingPassportedAssessment(any(), any());
        doNothing().when(financialAssessmentRepository).replaceAllByRepId(any());
        doNothing().when(hardshipReviewRepository).replaceAllByRepId(any());

        passportAssessmentPersistenceService.create(TestEntityDataBuilder.getPassportAssessmentEntity());

        verify(passportAssessmentRepository).save(any());
        verify(passportAssessmentRepository).replaceAllByRepIdExcludingPassportedAssessment(any(), any());
        verify(financialAssessmentRepository).replaceAllByRepId(any());
        verify(hardshipReviewRepository).replaceAllByRepId(any());
    }
}
