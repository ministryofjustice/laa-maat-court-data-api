package gov.uk.courtdata.passport.service;

import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_PASSPORT_ASSESSMENT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.passport.mapper.PassportAssessmentMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PassportAssessmentServiceV2Test {

    @InjectMocks
    private PassportAssessmentServiceV2 passportAssessmentService;

    @Mock
    private PassportAssessmentPersistenceService passportAssessmentPersistenceService;

    @Mock
    private PassportAssessmentMapper passportAssessmentMapper;
    
    @Test
    void whenFindByLegacyIdIsInvoked_thenPassportedAssessmentIsRetrieved() {
        var apiGetPassportedAssessmentResponse = TestModelDataBuilder.getApiGetPassportedAssessmentResponse();
        when(passportAssessmentPersistenceService.find(any())).thenReturn(TestEntityDataBuilder.getPassportAssessmentEntity());
        when(passportAssessmentMapper.toApiGetPassportedAssessmentResponse(any(
            PassportAssessmentEntity.class))).thenReturn(apiGetPassportedAssessmentResponse);
        var returnedPassportedAssessment = passportAssessmentService.find(LEGACY_PASSPORT_ASSESSMENT_ID);
        assertEquals(LEGACY_PASSPORT_ASSESSMENT_ID, returnedPassportedAssessment.getLegacyAssessmentId());
    }
}
