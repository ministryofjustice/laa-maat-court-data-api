package gov.uk.courtdata.integration.applicant;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.SEND_TO_CCLF;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.applicant.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.entity.ApplicantHistoryEntity;
import gov.uk.courtdata.applicant.service.ApplicantHistoryService;
import gov.uk.courtdata.applicant.service.RepOrderApplicantLinksService;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class ApplicantServiceIntegrationTest extends MockMvcIntegrationTest {

    private static final int ID = 1;

    @Autowired
    private RepOrderApplicantLinksService repOrderApplicantLinksService;

    @Autowired
    private ApplicantHistoryService applicantHistoryService;

    @Test
    void givenValidRepId_WhenGetRepOrderApplicantLinksIsInvoked_thenCorrectResponseIsReturned() {
        repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID));
        repos.repOrderApplicantLinks.saveAndFlush(
            TestEntityDataBuilder.getRepOrderApplicantLinksEntity());
        List<RepOrderApplicantLinksDTO> result = repOrderApplicantLinksService.find(REP_ID);
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.get(0).getId()).isGreaterThan(0);
        assertThat(result.get(0).getRepId()).isEqualTo(REP_ID);
    }

    @Test
    void givenInValidRepId_WhenGetRepOrderApplicantLinksIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            repOrderApplicantLinksService.find(REP_ID);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Rep Order Applicant Links not found for repId");
    }

    @Test
    void givenAValidInput_whenUpdateRepOrderApplicantLinksIsInvoked_thenUpdateIsSuccess() {
        repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID));
        repos.repOrderApplicantLinks.saveAndFlush(
            TestEntityDataBuilder.getRepOrderApplicantLinksEntity());
        Integer id = repos.repOrderApplicantLinks.findAll().get(0).getId();
        RepOrderApplicantLinksDTO recordToUpdate = TestModelDataBuilder.getRepOrderApplicantLinksDTO(id);
        RepOrderApplicantLinksDTO repOrderApplicantLinksDTO = repOrderApplicantLinksService.update(recordToUpdate);
        assertThat(repOrderApplicantLinksDTO.getId()).isEqualTo(id);
        assertThat(repOrderApplicantLinksDTO.getUserModified()).isEqualTo(recordToUpdate.getUserModified());
        assertThat(repOrderApplicantLinksDTO.getUnlinkDate()).isEqualTo(recordToUpdate.getUnlinkDate());
    }

    @Test
    void givenAInValidInput_whenUpdateRepOrderApplicantLinksIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            repOrderApplicantLinksService.update(TestModelDataBuilder.getRepOrderApplicantLinksDTO(ID));
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Rep Order Applicant Link not found for id");
    }

    @Test
    void givenValidId_whenGetApplicantHistoryIsInvoked_thenCorrectResponseIsReturned() {
        ApplicantHistoryEntity testRecord = TestEntityDataBuilder.getApplicantHistoryEntity("N");
        repos.applicantHistory.saveAndFlush(testRecord);
        Integer id = repos.applicantHistory.findAll().get(0).getId();
        ApplicantHistoryDTO result = applicantHistoryService.find(id);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isGreaterThan(0);
        assertThat(result.getApplId()).isEqualTo(testRecord.getApplId());
    }

    @Test
    void givenInValidId_whenGetApplicantHistoryIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            applicantHistoryService.find(ID);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant History not found for id");
    }

    @Test
    void givenAValidInput_whenUpdateApplicantHistoryIsInvoked_thenUpdateIsSuccess() {
        repos.applicantHistory.saveAndFlush(TestEntityDataBuilder.getApplicantHistoryEntity("N"));
        Integer id = repos.applicantHistory.findAll().get(0).getId();
        ApplicantHistoryDTO applicantHistoryDTO = applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO(id, SEND_TO_CCLF));
        assertThat(applicantHistoryDTO.getId()).isEqualTo(id);
        assertThat(applicantHistoryDTO.getSendToCclf()).isEqualTo(SEND_TO_CCLF);
    }

    @Test
    void givenAInValidInput_whenUpdateApplicantHistoryIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO(ID, "N"));
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant History not found for id");
    }
}
