package gov.uk.courtdata.passport.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import gov.uk.courtdata.exception.ValidationException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;
import uk.gov.justice.laa.crime.enums.evidence.IncomeEvidenceType;

@ExtendWith(MockitoExtension.class)
class PassportAssessmentEvidenceMapperTest {

    private static final Integer APPLICANT_ID = 123;
    private static final Integer INVALID_APPLICANT_ID = 786;
    private static final Integer PARTNER_ID = 456;
    LocalDateTime dateNow = LocalDateTime.now();
    
    PassportAssessmentEvidenceMapper passportAssessmentEvidenceMapper = new PassportAssessmentEvidenceMapperImpl();
    
    @Test
    void givenPassportAssessmentEntity_whenMapToApiGetPassportEvidenceResponse_thenAllFieldsMapped() {
        PassportAssessmentEntity passportAssessmentEntity = buildPassportAssessment();
        Applicant applicant = buildApplicant();
        PassportAssessmentEvidenceEntity passportAssessmentApplicantEvidenceEntity = 
            buildApplicantEvidence(applicant, passportAssessmentEntity);
        Applicant partner = buildPartner();
        PassportAssessmentEvidenceEntity passportAssessmentPartnerEvidenceEntity = 
            buildPartnerEvidence(partner, passportAssessmentEntity);
        
        ApiGetPassportEvidenceResponse response = 
            passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(
            passportAssessmentEntity, passportAssessmentPartnerEvidenceEntity.getApplicant().getId());

        // Metadata
        assertThat(response.getPassportEvidenceMetadata().getEvidenceDueDate()).isEqualTo(passportAssessmentEntity.getPassportEvidenceDueDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getEvidenceReceivedDate()).isEqualTo(passportAssessmentEntity.getAllPassportEvidenceReceivedDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getUpliftAppliedDate()).isEqualTo(passportAssessmentEntity.getPassportUpliftApplyDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getUpliftRemovedDate()).isEqualTo(passportAssessmentEntity.getPassportUpliftRemoveDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getFirstReminderDate()).isEqualTo(passportAssessmentEntity.getFirstPassportReminderDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getSecondReminderDate()).isEqualTo(passportAssessmentEntity.getSecondPassportReminderDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getIncomeEvidenceNotes()).isEqualTo(passportAssessmentEntity.getPassportEvidenceNotes());
        
        // Applicant evidence
        assertThat(response.getApplicantEvidenceItems().getFirst().getId()).isEqualTo(passportAssessmentApplicantEvidenceEntity.getId());
        assertThat(response.getApplicantEvidenceItems().getFirst().getDateReceived()).isEqualTo(passportAssessmentApplicantEvidenceEntity.getDateReceived().toLocalDate());
        assertThat(response.getApplicantEvidenceItems().getFirst().getEvidenceType()).isEqualTo(IncomeEvidenceType.getFrom(passportAssessmentApplicantEvidenceEntity.getIncomeEvidence()));
        assertThat(response.getApplicantEvidenceItems().getFirst().getMandatory()).isEqualTo(passportAssessmentApplicantEvidenceEntity.getMandatory().equals("Y"));
        assertThat(response.getApplicantEvidenceItems().getFirst().getDescription()).isEqualTo(passportAssessmentApplicantEvidenceEntity.getOtherText());
        
        // Partner evidence
        assertThat(response.getPartnerEvidenceItems().getFirst().getId()).isEqualTo(passportAssessmentPartnerEvidenceEntity.getId());
        assertThat(response.getPartnerEvidenceItems().getFirst().getDateReceived()).isEqualTo(passportAssessmentPartnerEvidenceEntity.getDateReceived().toLocalDate());
        assertThat(response.getPartnerEvidenceItems().getFirst().getEvidenceType()).isEqualTo(IncomeEvidenceType.getFrom(passportAssessmentPartnerEvidenceEntity.getIncomeEvidence()));
        assertThat(response.getPartnerEvidenceItems().getFirst().getMandatory()).isEqualTo(passportAssessmentPartnerEvidenceEntity.getMandatory().equals("Y"));
        assertThat(response.getPartnerEvidenceItems().getFirst().getDescription()).isEqualTo(passportAssessmentPartnerEvidenceEntity.getOtherText());
    }

    @Test
    void givenPassportAssessmentEntityWithNoPartnerEvidence_whenMapToApiGetPassportEvidenceResponse_thenNoPartnerEvidenceMapped() {
        PassportAssessmentEntity passportAssessmentEntity = buildPassportAssessment();
        Applicant applicant = buildApplicant();
        buildApplicantEvidence(applicant, passportAssessmentEntity);

        ApiGetPassportEvidenceResponse response = passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(passportAssessmentEntity, null);
        
        assertThat(response.getPartnerEvidenceItems().isEmpty());
    }

    @Test
    void givenPassportAssessmentEntityWithMultipleEvidenceItems_whenMapToApiGetPassportEvidenceResponse_thenAllEvidenceMapped() {
        PassportAssessmentEntity passportAssessmentEntity = buildPassportAssessment();
        Applicant applicant = buildApplicant();
        // First evidence for applicant
        buildApplicantEvidence(applicant, passportAssessmentEntity);
        // Second evidence for applicant
        buildApplicantEvidence(applicant, passportAssessmentEntity);
        Applicant partner = buildPartner();
        // First evidence for partner
        buildPartnerEvidence(partner, passportAssessmentEntity);
        // Second evidence for partner
        buildPartnerEvidence(partner, passportAssessmentEntity);

        ApiGetPassportEvidenceResponse response =
            passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(
                passportAssessmentEntity,
                partner.getId());
        
        assertThat(response.getApplicantEvidenceItems().size()).isEqualTo(2);
        assertThat(response.getPartnerEvidenceItems().size()).isEqualTo(2);
    }
    
    @Test
    void givenPassportAssessmentEvidenceEntityWithDifferentApplicantIdToRepOrder_whenMapToApiGetPassportEvidenceResponse_thenValidationExceptionThrown() {
        PassportAssessmentEntity passportAssessmentEntity = buildPassportAssessment();
        passportAssessmentEntity.getRepOrder().setApplicationId(INVALID_APPLICANT_ID);
        Applicant applicant = buildApplicant();
        buildApplicantEvidence(applicant, passportAssessmentEntity);

        assertThatThrownBy(() -> passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(
            passportAssessmentEntity, null))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Partner ID or Applicant ID does not match ID on evidence entity");
    }

    private PassportAssessmentEntity buildPassportAssessment() {
        PassportAssessmentEntity passportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        passportAssessmentEntity.getRepOrder().setApplicationId(APPLICANT_ID);
        passportAssessmentEntity.setPassportEvidenceDueDate(dateNow);
        passportAssessmentEntity.setAllPassportEvidenceReceivedDate(dateNow);
        passportAssessmentEntity.setPassportUpliftApplyDate(dateNow);
        passportAssessmentEntity.setPassportUpliftRemoveDate(dateNow);
        passportAssessmentEntity.setFirstPassportReminderDate(dateNow);
        passportAssessmentEntity.setSecondPassportReminderDate(dateNow);
        passportAssessmentEntity.setPassportEvidenceNotes("Evidence notes");

        return passportAssessmentEntity;
    }

    private Applicant buildApplicant() {
        return Applicant.builder()
            .id(APPLICANT_ID)
            .build();
    }

    private PassportAssessmentEvidenceEntity buildApplicantEvidence(Applicant applicant, PassportAssessmentEntity passportAssessmentEntity) {
        PassportAssessmentEvidenceEntity passportAssessmentApplicantEvidenceEntity =
            TestEntityDataBuilder.getPassportAssessmentEvidenceEntity(passportAssessmentEntity, applicant, dateNow);

        passportAssessmentEntity.addPassportAssessmentEvidences(passportAssessmentApplicantEvidenceEntity);

        return passportAssessmentApplicantEvidenceEntity;
    }

    private Applicant buildPartner() {
        return Applicant.builder()
            .id(PARTNER_ID)
            .build();
    }

    private PassportAssessmentEvidenceEntity buildPartnerEvidence(Applicant partner,
        PassportAssessmentEntity passportAssessmentEntity) {

        PassportAssessmentEvidenceEntity passportAssessmentPartnerEvidenceEntity =
            TestEntityDataBuilder.getPassportAssessmentEvidenceEntity(passportAssessmentEntity, partner, dateNow);
        passportAssessmentPartnerEvidenceEntity.setIncomeEvidence("OTHER_ADHOC");

        passportAssessmentEntity.addPassportAssessmentEvidences(passportAssessmentPartnerEvidenceEntity);

        return passportAssessmentPartnerEvidenceEntity;
    }
}
