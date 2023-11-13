package gov.uk.courtdata.integration.preupdatechecks;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.preupdatechecks.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.preupdatechecks.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.preupdatechecks.repository.ApplicantHistoryRepository;
import gov.uk.courtdata.preupdatechecks.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.preupdatechecks.service.ApplicantHistoryService;
import gov.uk.courtdata.preupdatechecks.service.RepOrderApplicantLinksService;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.RepositoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@Slf4j
@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class PreUpdateChecksServiceIntegrationTest {

    @Autowired
    private RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;

    @Autowired
    private RepOrderRepository repOrderRepository;

    @Autowired
    private ApplicantHistoryRepository applicantHistoryRepository;

    @Autowired
    private RepOrderApplicantLinksService repOrderApplicantLinksService;

    @Autowired
    private ApplicantHistoryService applicantHistoryService;

    @AfterEach
    public void clearUp() {
        RepositoryUtil.clearUp(repOrderApplicantLinksRepository, repOrderRepository, applicantHistoryRepository);
    }

    @Test
    void givenValidRepId_WhenGetRepOrderApplicantLinksIsInvoked_thenCorrectResponseIsReturned() {
        repOrderRepository.save(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID));
        repOrderApplicantLinksRepository.saveAndFlush(TestEntityDataBuilder.getRepOrderApplicantLinksEntity());
        List<RepOrderApplicantLinksDTO> result = repOrderApplicantLinksService.getRepOrderApplicantLinks(REP_ID);
        assertTrue(!result.isEmpty());
        assertTrue(result.get(0).getId() > 0);
        assertEquals(REP_ID, result.get(0).getRepId());
    }

    @Test
    void givenInValidRepId_WhenGetRepOrderApplicantLinksIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            repOrderApplicantLinksService.getRepOrderApplicantLinks(REP_ID);
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Rep Order Applicant Links not found for repId");
    }

    @Test
    void givenAValidInput_whenUpdateApplicantHistoryIsInvoked_thenUpdateIsSuccess() {
        applicantHistoryRepository.saveAndFlush(TestEntityDataBuilder.getApplicantHistoryEntity("N"));
        Integer id = applicantHistoryRepository.findAll().get(0).getId();
        String sendToCclf = "Y";
        ApplicantHistoryDTO applicantHistoryDTO = applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO(id, sendToCclf));
        assertEquals(id, applicantHistoryDTO.getId());
        assertEquals(sendToCclf, applicantHistoryDTO.getSendToCclf());
    }

    @Test
    void givenAInValidInput_whenUpdateApplicantHistoryIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO(1, "N"));
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant History not found for id");
    }

}
