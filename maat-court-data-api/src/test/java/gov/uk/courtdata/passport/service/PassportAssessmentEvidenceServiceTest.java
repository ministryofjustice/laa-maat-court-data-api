package gov.uk.courtdata.passport.service;

import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_PASSPORT_ASSESSMENT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.applicant.service.PartnerResolver;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import gov.uk.courtdata.passport.mapper.PassportAssessmentEvidenceMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;
import uk.gov.justice.laa.crime.common.model.evidence.ApiIncomeEvidence;
import uk.gov.justice.laa.crime.enums.evidence.IncomeEvidenceType;

@ExtendWith(MockitoExtension.class)
class PassportAssessmentEvidenceServiceTest {
    
    private static final Integer APPLICANT_ID = 111111;
    private static final Integer PARTNER_ID = 222222;
    private PassportAssessmentEntity passportAssessmentEntity;
    private PassportAssessmentEvidenceEntity applicantEvidenceEntity;
    private PassportAssessmentEvidenceEntity partnerEvidenceEntity;

    @InjectMocks
    private PassportAssessmentEvidenceService passportAssessmentEvidenceService;

    @Mock
    private PassportAssessmentPersistenceService passportAssessmentPersistenceService;

    @Mock
    private PassportAssessmentEvidenceMapper passportAssessmentEvidenceMapper;
    
    @Mock
    private PartnerResolver partnerResolver;

    @Test
    void whenFindIsInvoked_thenPassportEvidenceIsRetrieved() {
        buildEntities();
        
        ApiGetPassportEvidenceResponse apiGetPassportEvidenceResponse = TestModelDataBuilder.getApiGetPassportedEvidenceResponse();
        ApiIncomeEvidence applicantApiGetIncomeEvidenceResponse = new ApiIncomeEvidence()
            .withId(1)
            .withDescription("Applicant evidence")
            .withEvidenceType(IncomeEvidenceType.NINO)
            .withDateReceived(LocalDate.now().minusDays(2))
            .withMandatory(true);

        ApiIncomeEvidence partnerApiGetIncomeEvidenceResponse = new ApiIncomeEvidence()
            .withId(2)
            .withDescription("Partner evidence")
            .withEvidenceType(IncomeEvidenceType.OTHER_ADHOC)
            .withDateReceived(LocalDate.now().minusDays(1))
            .withMandatory(true);
        
        when(passportAssessmentPersistenceService.find(any())).thenReturn(passportAssessmentEntity);
        when(partnerResolver.getPartnerLegacyId(any())).thenReturn(PARTNER_ID);
        when(passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(any())).thenReturn(apiGetPassportEvidenceResponse);
        when(passportAssessmentEvidenceMapper.toApiIncomeEvidence(applicantEvidenceEntity)).thenReturn(applicantApiGetIncomeEvidenceResponse);
        when(passportAssessmentEvidenceMapper.toApiIncomeEvidence(partnerEvidenceEntity)).thenReturn(partnerApiGetIncomeEvidenceResponse);
        
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
    
    void buildEntities() {
        passportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        Applicant applicant = Applicant.builder().id(APPLICANT_ID).build();
        Applicant partner = Applicant.builder().id(PARTNER_ID).build();
        passportAssessmentEntity.getRepOrder().setApplicationId(applicant.getId());
        
        applicantEvidenceEntity =
            TestEntityDataBuilder.getPassportAssessmentEvidenceEntity(
                passportAssessmentEntity, applicant, LocalDateTime.now());

        partnerEvidenceEntity =
            TestEntityDataBuilder.getPassportAssessmentEvidenceEntity(
                passportAssessmentEntity, partner, LocalDateTime.now());

        passportAssessmentEntity.addPassportAssessmentEvidences(applicantEvidenceEntity);
        passportAssessmentEntity.addPassportAssessmentEvidences(partnerEvidenceEntity);
    }
}
