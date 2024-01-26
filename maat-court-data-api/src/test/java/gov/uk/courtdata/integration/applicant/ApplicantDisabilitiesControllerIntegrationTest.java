package gov.uk.courtdata.integration.applicant;

import gov.uk.MAATCourtDataApplication;
import gov.uk.courtdata.applicant.dto.ApplicantDisabilitiesDTO;
import gov.uk.courtdata.applicant.entity.ApplicantDisabilitiesEntity;
import gov.uk.courtdata.applicant.repository.ApplicantDisabilitiesRepository;
import gov.uk.courtdata.builder.TestEntityDataBuilder;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.integration.util.MockMvcIntegrationTest;
import gov.uk.courtdata.integration.util.RepositoryUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {MAATCourtDataApplication.class})
public class ApplicantDisabilitiesControllerIntegrationTest extends MockMvcIntegrationTest {

    private static final Integer INVALID_ID = 234;
    private static final String ENDPOINT_URL = "/api/internal/v1/applicant/applicant-disabilities";
    private static final int ID = 1;

    @Autowired
    private ApplicantDisabilitiesRepository applicantDisabilitiesRepository;

    @AfterEach
    public void tearDown() {
        new RepositoryUtil().clearUp(applicantDisabilitiesRepository);
    }

    @Test
    void givenCorrectId_whenGetApplicantDisabilitiesIsInvoked_thenResponseIsReturned() throws Exception {
        createApplicantDisabilities();
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenIncorrectId_whenGetApplicantDisabilitiesIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + INVALID_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenValidRequest_whenUpdateApplicantDisabilitiesIsInvoked_thenUpdateIsSuccess() throws Exception {
        createApplicantDisabilities();
        ApplicantDisabilitiesDTO recordToUpdate = TestModelDataBuilder.getApplicantDisabilitiesDTO(ID);
        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(recordToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(recordToUpdate.getId()))
                .andExpect(jsonPath("$.userModified").value(recordToUpdate.getUserModified()))
                .andExpect(jsonPath("$.applId").value(recordToUpdate.getApplId().toString()))
                .andExpect(jsonPath("$.disaDisability").value(recordToUpdate.getDisaDisability().toString()));
    }

    @Test
    void givenInValidRequest_whenUpdateRepOrderApplicantLinksIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantDisabilitiesDTO(INVALID_ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenValidRequest_whenCreateApplicantDisabilitiesIsInvoked_thenUpdateIsSuccess() throws Exception {
        ApplicantDisabilitiesDTO recordToCreate = TestModelDataBuilder.getApplicantDisabilitiesDTO(10);
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(recordToCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(recordToCreate.getId()))
                .andExpect(jsonPath("$.userModified").value(recordToCreate.getUserModified()))
                .andExpect(jsonPath("$.applId").value(recordToCreate.getApplId().toString()))
                .andExpect(jsonPath("$.disaDisability").value(recordToCreate.getDisaDisability().toString()));
    }

    @Test
    void givenValidRequest_whenDeleteApplicantDisabilitiesIsInvoked_thenUpdateIsSuccess() throws Exception {
        createApplicantDisabilities();
        mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL + "/" + ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private ApplicantDisabilitiesEntity createApplicantDisabilities() {
        return applicantDisabilitiesRepository.save(TestEntityDataBuilder.getApplicantDisabilitiesEntity(ID));
    }

}
