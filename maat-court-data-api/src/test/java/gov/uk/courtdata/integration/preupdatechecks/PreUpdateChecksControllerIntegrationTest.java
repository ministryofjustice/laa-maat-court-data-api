package gov.uk.courtdata.integration.preupdatechecks;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.preupdatechecks.repository.ApplicantHistoryRepository;
import gov.uk.courtdata.preupdatechecks.repository.RepOrderApplicantLinksRepository;
import gov.uk.courtdata.repository.RepOrderRepository;
import gov.uk.courtdata.util.RepositoryUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest(classes = {MAATCourtDataApplication.class})
@AutoConfigureMockMvc
public class PreUpdateChecksControllerIntegrationTest {

    private static final Integer INVALID_REP_ID = 234;
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/application/pre-update-checks";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepOrderApplicantLinksRepository repOrderApplicantLinksRepository;

    @Autowired
    private RepOrderRepository repOrderRepository;

    @Autowired
    private ApplicantHistoryRepository applicantHistoryRepository;

    @Autowired
    MockMvc mvc;

    @AfterEach
    public void clearUp() {
        RepositoryUtil.clearUp(repOrderApplicantLinksRepository, repOrderRepository, applicantHistoryRepository);
    }

    @Test
    void givenCorrectRepId_whenGetRepOrderApplicantLinksIsInvoked_thenResponseIsReturned() throws Exception {
        repOrderRepository.save(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID));
        repOrderApplicantLinksRepository.saveAndFlush(TestEntityDataBuilder.getRepOrderApplicantLinksEntity());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/repId/" + REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenIncorrectRepId_whenGetRepOrderApplicantLinksIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/repId/" + INVALID_REP_ID))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidRequest_whenUpdateApplicantHistoryIsInvoked_thenUpdateIsSuccess() throws Exception {
        applicantHistoryRepository.saveAndFlush(TestEntityDataBuilder.getApplicantHistoryEntity("N"));
        Integer id = applicantHistoryRepository.findAll().get(0).getId();
        String sendToCclf = "Y";
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(id, sendToCclf)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.sendToCclf").value(sendToCclf));
    }

    @Test
    void givenAEmptyContent_whenUpdateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history").content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInValidRequest_whenUpdateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(1, "Y")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

}
