package gov.uk.courtdata.passport.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.justice.laa.crime.common.model.evidence.ApiGetPassportEvidenceResponse;
import uk.gov.justice.laa.crime.common.model.evidence.ApiIncomeEvidence;
import uk.gov.justice.laa.crime.enums.evidence.IncomeEvidenceType;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    PassportAssessmentEvidenceMapperImpl.class,
    PassportAssessmentMapperHelper.class
})
class PassportAssessmentEvidenceMapperTest {

    private static final Integer APPLICANT_ID = 123;
    LocalDateTime dateNow = LocalDateTime.now();

    @Autowired
    private PassportAssessmentEvidenceMapper passportAssessmentEvidenceMapper;
    
    @Autowired
    private PassportAssessmentMapperHelper passportAssessmentMapperHelper;
    
    @Test
    void givenPassportAssessmentEntity_whenMapToApiGetPassportEvidenceResponse_thenMetadataIsMapped() {
        PassportAssessmentEntity passportAssessmentEntity = buildPassportAssessment();
        
        ApiGetPassportEvidenceResponse response = 
            passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(
            passportAssessmentEntity);

        // Metadata
        assertThat(response.getPassportEvidenceMetadata().getEvidenceDueDate()).isEqualTo(passportAssessmentEntity.getPassportEvidenceDueDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getEvidenceReceivedDate()).isEqualTo(passportAssessmentEntity.getAllPassportEvidenceReceivedDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getUpliftAppliedDate()).isEqualTo(passportAssessmentEntity.getPassportUpliftApplyDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getUpliftRemovedDate()).isEqualTo(passportAssessmentEntity.getPassportUpliftRemoveDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getFirstReminderDate()).isEqualTo(passportAssessmentEntity.getFirstPassportReminderDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getSecondReminderDate()).isEqualTo(passportAssessmentEntity.getSecondPassportReminderDate().toLocalDate());
        assertThat(response.getPassportEvidenceMetadata().getIncomeEvidenceNotes()).isEqualTo(passportAssessmentEntity.getPassportEvidenceNotes());
    }

    @Test
    void givenPassportAssessmentEvidenceEntity_whenMapToApiIncomeEvidence_thenEvidenceIsMapped() {
        PassportAssessmentEntity passportAssessmentEntity = buildPassportAssessment();
        Applicant applicant = buildApplicant();
        PassportAssessmentEvidenceEntity evidenceEntity =
            buildApplicantEvidence(applicant, passportAssessmentEntity);

        ApiIncomeEvidence response =
            passportAssessmentEvidenceMapper.toApiIncomeEvidence(
                evidenceEntity);
        
        assertThat(response.getId()).isEqualTo(evidenceEntity.getId());
        assertThat(response.getDateReceived()).isEqualTo(evidenceEntity.getDateReceived().toLocalDate());
        assertThat(response.getEvidenceType()).isEqualTo(IncomeEvidenceType.getFrom(evidenceEntity.getIncomeEvidence()));
        assertThat(response.getMandatory()).isEqualTo(evidenceEntity.getMandatory().equals("Y"));
        assertThat(response.getDescription()).isEqualTo(evidenceEntity.getOtherText());
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
}
