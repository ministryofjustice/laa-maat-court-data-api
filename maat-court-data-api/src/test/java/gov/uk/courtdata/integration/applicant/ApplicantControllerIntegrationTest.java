package gov.uk.courtdata.integration.applicant;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.SEND_TO_CCLF;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.entity.RepOrderApplicantLinksEntity;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.RepOrderEntity;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class ApplicantControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final Integer INVALID_REP_ID = 234;
    private static final String ENDPOINT_URL = "/api/internal/v1/application/applicant";
    private static final int ID = 1;

    @Test
    void givenCorrectRepId_whenGetRepOrderApplicantLinksIsInvoked_thenResponseIsReturned() throws Exception {
        RepOrderEntity repOrderEntity = repos.repOrder.save(
                TestEntityDataBuilder.getPopulatedRepOrder());
        RepOrderApplicantLinksEntity repOrderApplicantLinks = TestEntityDataBuilder.getRepOrderApplicantLinksEntity();
        repOrderApplicantLinks.setRepId(repOrderEntity.getId());
        repos.repOrderApplicantLinks.saveAndFlush(repOrderApplicantLinks);
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/" + repOrderEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenIncorrectRepId_whenGetRepOrderApplicantLinksIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/" + INVALID_REP_ID))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidRequest_whenUpdateRepOrderApplicantLinksIsInvoked_thenUpdateIsSuccess() throws Exception {
        repos.repOrder.save(TestEntityDataBuilder.getPopulatedRepOrder(REP_ID));
        repos.repOrderApplicantLinks.saveAndFlush(
                TestEntityDataBuilder.getRepOrderApplicantLinksEntity());
        Integer id = repos.repOrderApplicantLinks.findAll().get(0).getId();
        RepOrderApplicantLinksDTO recordToUpdate = TestModelDataBuilder.getRepOrderApplicantLinksDTO(id);
        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/rep-order-applicant-links")
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
        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/rep-order-applicant-links")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getRepOrderApplicantLinksDTO(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenCorrectId_whenGetApplicantHistoryIsInvoked_thenResponseIsReturned() throws Exception {
        repos.applicantHistory.saveAndFlush(TestEntityDataBuilder.getApplicantHistoryEntity("N"));
        Integer id = repos.applicantHistory.findAll().get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/applicant-history/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenIncorrectId_whenGetApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/applicant-history/" + ID))
                .andExpect(status().is(404));
    }

    @Test
    void givenValidRequest_whenUpdateApplicantHistoryIsInvoked_thenUpdateIsSuccess() throws Exception {
        repos.applicantHistory.saveAndFlush(TestEntityDataBuilder.getApplicantHistoryEntity("N"));
        Integer id = repos.applicantHistory.findAll().get(0).getId();
        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(id, SEND_TO_CCLF)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.sendToCclf").value(SEND_TO_CCLF));
    }

    @Test
    void givenInValidRequest_whenUpdateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(ID, "Y")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenCorrectRepId_whenGetRepOrderApplicantLinksIsInvoked_thenApplicationHistoryResponse() throws Exception {
        RepOrderEntity repOrderEntity = repos.repOrder.save(
                TestEntityDataBuilder.getPopulatedRepOrder());
        RepOrderApplicantLinksEntity repOrderApplicantLinks = TestEntityDataBuilder.getRepOrderApplicantLinksEntity();
        repOrderApplicantLinks.setRepId(repOrderEntity.getId());
        repOrderApplicantLinks.setAphi(TestEntityDataBuilder.getApplicantHistoryEntity("N"));
        repos.repOrderApplicantLinks.saveAndFlush(repOrderApplicantLinks);
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/" + repOrderEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].aphi").isNotEmpty())
                .andExpect(jsonPath("$[0].aphi.applId").value(716));
    }
}
