package gov.uk.courtdata.integration.applicant;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.applicant.repository.ApplicantHistoryRepository;
import gov.uk.courtdata.applicant.repository.RepOrderApplicantLinksRepository;
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
import static gov.uk.courtdata.builder.TestModelDataBuilder.SEND_TO_CCLF;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest(classes = {MAATCourtDataApplication.class})
@AutoConfigureMockMvc(addFilters = false)
public class ApplicantControllerIntegrationTest {

    private static final Integer INVALID_REP_ID = 234;
    private static final String ENDPOINT_URL = "/api/internal/v1/application/applicant";
    private static final int ID = 1;

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
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/" + REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenIncorrectRepId_whenGetRepOrderApplicantLinksIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/" + INVALID_REP_ID))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidRequest_whenUpdateRepOrderApplicantLinksIsInvoked_thenUpdateIsSuccess() throws Exception {
        repOrderRepository.save(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID));
        repOrderApplicantLinksRepository.saveAndFlush(TestEntityDataBuilder.getRepOrderApplicantLinksEntity());
        Integer id = repOrderApplicantLinksRepository.findAll().get(0).getId();
        RepOrderApplicantLinksDTO recordToUpdate = TestModelDataBuilder.getRepOrderApplicantLinksDTO(id);
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/rep-order-applicant-links")
                        .content(objectMapper.writeValueAsString(recordToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.userModified").value(recordToUpdate.getUserModified()))
                .andExpect(jsonPath("$.unlinkDate").value(recordToUpdate.getUnlinkDate().toString()));
    }

    @Test
    void givenInValidRequest_whenUpdateRepOrderApplicantLinksIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/rep-order-applicant-links")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getRepOrderApplicantLinksDTO(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenCorrectId_whenGetApplicantHistoryIsInvoked_thenResponseIsReturned() throws Exception {
        applicantHistoryRepository.saveAndFlush(TestEntityDataBuilder.getApplicantHistoryEntity("N"));
        Integer id = applicantHistoryRepository.findAll().get(0).getId();
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/applicant-history/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenIncorrectId_whenGetApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/applicant-history/" + ID))
                .andExpect(status().is(404));
    }

    @Test
    void givenValidRequest_whenUpdateApplicantHistoryIsInvoked_thenUpdateIsSuccess() throws Exception {
        applicantHistoryRepository.saveAndFlush(TestEntityDataBuilder.getApplicantHistoryEntity("N"));
        Integer id = applicantHistoryRepository.findAll().get(0).getId();
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(id, SEND_TO_CCLF)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.sendToCclf").value(SEND_TO_CCLF));
    }

    @Test
    void givenInValidRequest_whenUpdateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(ID, "Y")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

}
