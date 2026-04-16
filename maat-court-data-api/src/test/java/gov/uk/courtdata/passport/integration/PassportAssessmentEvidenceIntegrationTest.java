package gov.uk.courtdata.passport.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.entity.Applicant;
import gov.uk.courtdata.entity.PassportAssessmentEntity;
import gov.uk.courtdata.entity.PassportAssessmentEvidenceEntity;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.enums.evidence.IncomeEvidenceType;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
class PassportAssessmentEvidenceIntegrationTest extends MockMvcIntegrationTest {

    private static final String ENDPOINT_URL = "/api/internal/v2/assessment/passport-assessments/{id}/evidence";
    private PassportAssessmentEntity passportAssessmentEntity;
    private PassportAssessmentEvidenceEntity applicantEvidenceEntity;
    private PassportAssessmentEvidenceEntity partnerEvidenceEntity;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void givenValidRequest_whenFindIsInvoked_thenPassportEvidenceIsReturned() throws Exception {
        buildEntities();
        
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL, passportAssessmentEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.passportEvidenceMetadata.evidenceDueDate").value(passportAssessmentEntity.getPassportEvidenceDueDate().toLocalDate().toString()))
            .andExpect(jsonPath("$.passportEvidenceMetadata.evidenceReceivedDate").value(passportAssessmentEntity.getAllPassportEvidenceReceivedDate().toLocalDate().toString()))
            .andExpect(jsonPath("$.passportEvidenceMetadata.upliftAppliedDate").value(passportAssessmentEntity.getPassportUpliftApplyDate().toLocalDate().toString()))
            .andExpect(jsonPath("$.passportEvidenceMetadata.upliftRemovedDate").value(passportAssessmentEntity.getPassportUpliftRemoveDate().toLocalDate().toString()))
            .andExpect(jsonPath("$.passportEvidenceMetadata.firstReminderDate").value(passportAssessmentEntity.getFirstPassportReminderDate().toLocalDate().toString()))
            .andExpect(jsonPath("$.passportEvidenceMetadata.secondReminderDate").value(passportAssessmentEntity.getSecondPassportReminderDate().toLocalDate().toString()))
            .andExpect(jsonPath("$.passportEvidenceMetadata.incomeEvidenceNotes").value(passportAssessmentEntity.getPassportEvidenceNotes()))
            .andExpect(jsonPath("$.applicantEvidenceItems[0].id").value(passportAssessmentEntity.getPassportAssessmentEvidences().get(0).getId()))
            .andExpect(jsonPath("$.applicantEvidenceItems[0].dateReceived").value(passportAssessmentEntity.getPassportAssessmentEvidences().get(0).getDateReceived().toLocalDate().toString()))
            .andExpect(jsonPath("$.applicantEvidenceItems[0].evidenceType").value(IncomeEvidenceType.getFrom(passportAssessmentEntity.getPassportAssessmentEvidences().get(0).getIncomeEvidence()).getName()))
            .andExpect(jsonPath("$.applicantEvidenceItems[0].mandatory").value(passportAssessmentEntity.getPassportAssessmentEvidences().get(0).getMandatory().equals("Y")))
            .andExpect(jsonPath("$.applicantEvidenceItems[0].description").value(passportAssessmentEntity.getPassportAssessmentEvidences().get(0).getOtherText()))
            .andExpect(jsonPath("$.partnerEvidenceItems[0].id").value(passportAssessmentEntity.getPassportAssessmentEvidences().get(1).getId()))
            .andExpect(jsonPath("$.partnerEvidenceItems[0].dateReceived").value(passportAssessmentEntity.getPassportAssessmentEvidences().get(1).getDateReceived().toLocalDate().toString()))
            .andExpect(jsonPath("$.partnerEvidenceItems[0].evidenceType").value(IncomeEvidenceType.getFrom(passportAssessmentEntity.getPassportAssessmentEvidences().get(1).getIncomeEvidence()).getName()))
            .andExpect(jsonPath("$.partnerEvidenceItems[0].mandatory").value(passportAssessmentEntity.getPassportAssessmentEvidences().get(1).getMandatory().equals("Y")))
            .andExpect(jsonPath("$.partnerEvidenceItems[0].description").value(passportAssessmentEntity.getPassportAssessmentEvidences().get(1).getOtherText()));
    }

    private void buildEntities() {
        LocalDateTime dateNow = LocalDateTime.now();

        Applicant applicantEntity = repos.applicantRepository.saveAndFlush(Applicant.builder()
            .build());

        Applicant partnerEntity = repos.applicantRepository.saveAndFlush(Applicant.builder()
            .build());

        RepOrderEntity repOrderEntity =  TestEntityDataBuilder.getPopulatedRepOrder();
        repOrderEntity.setApplicationId(applicantEntity.getId());
        repOrderEntity = repos.repOrder.saveAndFlush(repOrderEntity);

        // Link the partner to the rep order
        RepOrderApplicantLinksEntity repOrderApplicantLinksEntity = TestEntityDataBuilder.getRepOrderApplicantLinksEntity();
        repOrderApplicantLinksEntity.setRepId(repOrderEntity.getId());
        repOrderApplicantLinksEntity.setPartnerApplId(partnerEntity.getId());
        repOrderApplicantLinksEntity.setUnlinkDate(null);
        repos.repOrderApplicantLinks.saveAndFlush(repOrderApplicantLinksEntity);

        passportAssessmentEntity = TestEntityDataBuilder.getPassportAssessmentEntity();
        passportAssessmentEntity.getRepOrder().setId(repOrderEntity.getId());
        passportAssessmentEntity.setPassportEvidenceDueDate(dateNow);
        passportAssessmentEntity.setAllPassportEvidenceReceivedDate(dateNow);
        passportAssessmentEntity.setPassportUpliftApplyDate(dateNow);
        passportAssessmentEntity.setPassportUpliftRemoveDate(dateNow);
        passportAssessmentEntity.setFirstPassportReminderDate(dateNow);
        passportAssessmentEntity.setSecondPassportReminderDate(dateNow);
        passportAssessmentEntity.setPassportEvidenceNotes("Evidence notes");
        
        applicantEvidenceEntity =
            TestEntityDataBuilder.getPassportAssessmentEvidenceEntity(passportAssessmentEntity, applicantEntity, LocalDateTime.now());
        applicantEvidenceEntity.setId(null);
        applicantEvidenceEntity.setPassportAssessment(passportAssessmentEntity);
        passportAssessmentEntity.addPassportAssessmentEvidences(applicantEvidenceEntity);
        
        partnerEvidenceEntity =
            TestEntityDataBuilder.getPassportAssessmentEvidenceEntity(passportAssessmentEntity, partnerEntity, LocalDateTime.now());
        partnerEvidenceEntity.setId(null);
        partnerEvidenceEntity.setPassportAssessment(passportAssessmentEntity);
        passportAssessmentEntity.addPassportAssessmentEvidences(partnerEvidenceEntity);
        
        passportAssessmentEntity = repos.passportAssessment.saveAndFlush(passportAssessmentEntity);
    }
    
    @AfterEach
    void clearDatabase() {
        // We have to delete here in order to avoid foreign key violations when using the superclass repos.clearAll
        repos.passportAssessment.deleteAll();
        repos.passportAssessment.flush();
        
        repos.repOrder.deleteAllInBatch();
        repos.repOrder.flush();

        repos.applicantRepository.deleteAllInBatch();
        repos.applicantRepository.flush();
    }
}
