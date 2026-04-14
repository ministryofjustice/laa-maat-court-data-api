package gov.uk.courtdata.passport.service;

import static gov.uk.courtdata.builder.TestEntityDataBuilder.APPLICANT_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_PASSPORT_ASSESSMENT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.applicant.service.PartnerService;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.passport.mapper.PassportAssessmentEvidenceMapper;
import gov.uk.courtdata.repository.PassportAssessmentEvidenceRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;

@ExtendWith(MockitoExtension.class)
public class PassportAssessmentEvidenceServiceTest {
    
    @InjectMocks
    private PassportAssessmentEvidenceService passportAssessmentEvidenceService;

    @Mock
    private PassportAssessmentPersistenceService passportAssessmentPersistenceService;

    @Mock
    private PassportAssessmentEvidenceMapper passportAssessmentEvidenceMapper;

    @Mock
    private PassportAssessmentEvidenceRepository passportAssessmentEvidenceRepository;
    
    @Mock
    private PartnerService partnerService;

    @Test
    void whenFindIsInvoked_thenPassportEvidenceIsRetrieved() {
        Applicant applicant = Applicant.builder()
            .id(APPLICANT_ID)
            .build();

        PassportAssessmentEntity passportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        ApiGetPassportEvidenceResponse apiGetPassportEvidenceResponse = TestModelDataBuilder.getApiGetPassportedEvidenceResponse();
        when(passportAssessmentPersistenceService.find(any())).thenReturn(passportAssessmentEntity);
        when(passportAssessmentEvidenceRepository.findByPassportAssessment(any()))
            .thenReturn(List.of(TestEntityDataBuilder.getPassportAssessmentEvidenceEntity(passportAssessmentEntity, applicant,
                LocalDateTime.now())));
        when(partnerService.getPartnerLegacyId(any())).thenReturn(1);
        when(passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(any(), any(), any())).thenReturn(apiGetPassportEvidenceResponse);
        
        ApiGetPassportEvidenceResponse returnedPassportedAssessmentEvidence = passportAssessmentEvidenceService.find(LEGACY_PASSPORT_ASSESSMENT_ID);
        
        // Metadata
        assertEquals(apiGetPassportEvidenceResponse.getPassportEvidenceMetadata().getEvidenceDueDate(),
            returnedPassportedAssessmentEvidence.getPassportEvidenceMetadata().getEvidenceDueDate());
        assertEquals(apiGetPassportEvidenceResponse.getPassportEvidenceMetadata().getEvidenceReceivedDate(),
            returnedPassportedAssessmentEvidence.getPassportEvidenceMetadata().getEvidenceReceivedDate());
        assertEquals(apiGetPassportEvidenceResponse.getPassportEvidenceMetadata().getUpliftAppliedDate(),
            returnedPassportedAssessmentEvidence.getPassportEvidenceMetadata().getUpliftAppliedDate());
        assertEquals(apiGetPassportEvidenceResponse.getPassportEvidenceMetadata().getUpliftRemovedDate(),
            returnedPassportedAssessmentEvidence.getPassportEvidenceMetadata().getUpliftRemovedDate());
        assertEquals(apiGetPassportEvidenceResponse.getPassportEvidenceMetadata().getFirstReminderDate(),
            returnedPassportedAssessmentEvidence.getPassportEvidenceMetadata().getFirstReminderDate());
        assertEquals(apiGetPassportEvidenceResponse.getPassportEvidenceMetadata().getSecondReminderDate(),
            returnedPassportedAssessmentEvidence.getPassportEvidenceMetadata().getSecondReminderDate());
        assertEquals(apiGetPassportEvidenceResponse.getPassportEvidenceMetadata().getIncomeEvidenceNotes(),
            returnedPassportedAssessmentEvidence.getPassportEvidenceMetadata().getIncomeEvidenceNotes());

        // Applicant evidence
        assertEquals(apiGetPassportEvidenceResponse.getApplicantEvidenceItems().getFirst().getId(),
            returnedPassportedAssessmentEvidence.getApplicantEvidenceItems().getFirst().getId());
        assertEquals(apiGetPassportEvidenceResponse.getApplicantEvidenceItems().getFirst().getEvidenceType(),
            returnedPassportedAssessmentEvidence.getApplicantEvidenceItems().getFirst().getEvidenceType());
        assertEquals(apiGetPassportEvidenceResponse.getApplicantEvidenceItems().getFirst().getDescription(),
            returnedPassportedAssessmentEvidence.getApplicantEvidenceItems().getFirst().getDescription());
        assertEquals(apiGetPassportEvidenceResponse.getApplicantEvidenceItems().getFirst().getDateReceived(),
            returnedPassportedAssessmentEvidence.getApplicantEvidenceItems().getFirst().getDateReceived());
        assertEquals(apiGetPassportEvidenceResponse.getApplicantEvidenceItems().getFirst().getMandatory(),
            returnedPassportedAssessmentEvidence.getApplicantEvidenceItems().getFirst().getMandatory());

        // Partner evidence
        assertEquals(apiGetPassportEvidenceResponse.getPartnerEvidenceItems().getFirst().getId(),
            returnedPassportedAssessmentEvidence.getPartnerEvidenceItems().getFirst().getId());
        assertEquals(apiGetPassportEvidenceResponse.getPartnerEvidenceItems().getFirst().getEvidenceType(),
            returnedPassportedAssessmentEvidence.getPartnerEvidenceItems().getFirst().getEvidenceType());
        assertEquals(apiGetPassportEvidenceResponse.getPartnerEvidenceItems().getFirst().getDescription(),
            returnedPassportedAssessmentEvidence.getPartnerEvidenceItems().getFirst().getDescription());
        assertEquals(apiGetPassportEvidenceResponse.getPartnerEvidenceItems().getFirst().getDateReceived(),
            returnedPassportedAssessmentEvidence.getPartnerEvidenceItems().getFirst().getDateReceived());
        assertEquals(apiGetPassportEvidenceResponse.getPartnerEvidenceItems().getFirst().getMandatory(),
            returnedPassportedAssessmentEvidence.getPartnerEvidenceItems().getFirst().getMandatory());
    }
}
