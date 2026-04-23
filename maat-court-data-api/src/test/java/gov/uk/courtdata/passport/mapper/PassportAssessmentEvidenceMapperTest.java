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
import uk.gov.justice.laa.crime.common.model.evidence.ApiPassportEvidenceMetadata;
import uk.gov.justice.laa.crime.enums.evidence.IncomeEvidenceType;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    PassportAssessmentEvidenceMapperImpl.class,
    PassportAssessmentMapperHelper.class
})
class PassportAssessmentEvidenceMapperTest {

    private static final Integer APPLICANT_ID = 123;
    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2026, 4, 21, 12, 0);

    @Autowired
    private PassportAssessmentEvidenceMapper passportAssessmentEvidenceMapper;
    
    @Test
    void givenPassportAssessmentEntity_whenMapToApiGetPassportEvidenceResponse_thenMetadataIsMapped() {
        PassportAssessmentEntity passportAssessmentEntity = buildPassportAssessment();
        
        ApiGetPassportEvidenceResponse response = 
            passportAssessmentEvidenceMapper.toApiGetPassportEvidenceResponse(
            passportAssessmentEntity);

        assertThat(response.getPassportEvidenceMetadata())
            .extracting(
                ApiPassportEvidenceMetadata::getEvidenceDueDate,
                ApiPassportEvidenceMetadata::getEvidenceReceivedDate,
                ApiPassportEvidenceMetadata::getUpliftAppliedDate,
                ApiPassportEvidenceMetadata::getUpliftRemovedDate,
                ApiPassportEvidenceMetadata::getFirstReminderDate,
                ApiPassportEvidenceMetadata::getSecondReminderDate,
                ApiPassportEvidenceMetadata::getIncomeEvidenceNotes
            )
            .containsExactly(
                passportAssessmentEntity.getPassportEvidenceDueDate().toLocalDate(),
                passportAssessmentEntity.getAllPassportEvidenceReceivedDate().toLocalDate(),
                passportAssessmentEntity.getPassportUpliftApplyDate().toLocalDate(),
                passportAssessmentEntity.getPassportUpliftRemoveDate().toLocalDate(),
                passportAssessmentEntity.getFirstPassportReminderDate().toLocalDate(),
                passportAssessmentEntity.getSecondPassportReminderDate().toLocalDate(),
                passportAssessmentEntity.getPassportEvidenceNotes()
            );
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
        passportAssessmentEntity.setPassportEvidenceDueDate(DATE_TIME);
        passportAssessmentEntity.setAllPassportEvidenceReceivedDate(DATE_TIME);
        passportAssessmentEntity.setPassportUpliftApplyDate(DATE_TIME);
        passportAssessmentEntity.setPassportUpliftRemoveDate(DATE_TIME);
        passportAssessmentEntity.setFirstPassportReminderDate(DATE_TIME);
        passportAssessmentEntity.setSecondPassportReminderDate(DATE_TIME);
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
            TestEntityDataBuilder.getPassportAssessmentEvidenceEntity(passportAssessmentEntity, applicant, DATE_TIME);

        passportAssessmentEntity.addPassportAssessmentEvidences(passportAssessmentApplicantEvidenceEntity);

        return passportAssessmentApplicantEvidenceEntity;
    }
}
