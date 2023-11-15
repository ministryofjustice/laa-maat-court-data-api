package gov.uk.courtdata.integration.applicant;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.applicant.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.repository.ApplicantHistoryRepository;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.applicant.service.ApplicantHistoryService;
import gov.uk.courtdata.applicant.service.RepOrderApplicantLinksService;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.RepositoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.SEND_TO_CCLF;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@Slf4j
@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class ApplicantServiceIntegrationTest {

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
    void givenAValidInput_whenUpdateApplicantHistoryIsInvoked_thenUpdateIsSuccess() {
        applicantHistoryRepository.saveAndFlush(TestEntityDataBuilder.getApplicantHistoryEntity("N"));
        Integer id = applicantHistoryRepository.findAll().get(0).getId();
        ApplicantHistoryDTO applicantHistoryDTO = applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO(id, SEND_TO_CCLF));
        assertThat(applicantHistoryDTO.getId()).isEqualTo(id);
        assertThat(applicantHistoryDTO.getSendToCclf()).isEqualTo(SEND_TO_CCLF);
    }

    @Test
    void givenAInValidInput_whenUpdateApplicantHistoryIsInvoked_thenExceptionIsRaised() {
        assertThatThrownBy(() -> {
            applicantHistoryService.update(TestModelDataBuilder.getApplicantHistoryDTO(1, "N"));
        }).isInstanceOf(RequestedObjectNotFoundException.class)
                .hasMessageContaining("Applicant History not found for id");
    }

}
