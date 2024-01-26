package gov.uk.courtdata.applicant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.applicant.dto.ApplicantDisabilitiesDTO;
import gov.uk.courtdata.applicant.service.ApplicantDisabilitiesService;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.ErrorCodes;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicantDisabilitiesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ApplicantDisabilitiesControllerTest {

    private static final Integer INVALID_ID = 234;
    private static final String ENDPOINT_URL = "/api/internal/v1/applicant/applicant-disabilities";
    private static final int ID = 1;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ApplicantDisabilitiesService applicantDisabilitiesService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidId_whenGetApplicantDisabilitiesIsInvoked_thenResponseIsReturned() throws Exception {
        ApplicantDisabilitiesDTO response = TestModelDataBuilder.getApplicantDisabilitiesDTO(ID);
        when(applicantDisabilitiesService.find(ID))
                .thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(applicantDisabilitiesService).find(ID);
    }

    @Test
    void givenValidRequest_whenUpdateApplicantDisabilitiesIsInvoked_thenUpdateIsSuccess() throws Exception {
        ApplicantDisabilitiesDTO applicantDisabilitiesDTO = TestModelDataBuilder.getApplicantDisabilitiesDTO(ID);
        when(applicantDisabilitiesService.update(any()))
                .thenReturn(applicantDisabilitiesDTO);
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantDisabilitiesDTO(ID)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(applicantDisabilitiesService).update(applicantDisabilitiesDTO);

    }

    @Test
    void givenAEmptyContent_whenUpdateApplicantDisabilitiesIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInValidRequest_whenUpdateApplicantDisabilitiesIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(applicantDisabilitiesService.update(any()))
                .thenThrow(new RequestedObjectNotFoundException("Applicant Disabilities details not found"));
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantDisabilitiesDTO(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void givenInternalServerError_whenUpdateRepOrderApplicantLinkIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(applicantDisabilitiesService.update(any()))
                .thenThrow(EmptyResultDataAccessException.class);
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantDisabilitiesDTO(ID)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ErrorCodes.DB_ERROR));
    }

    @Test
    void givenValidRequest_whenCreateApplicantDisabilitiesIsInvoked_thenCreateIsSuccess() throws Exception {
        ApplicantDisabilitiesDTO applicantDisabilitiesDTO = TestModelDataBuilder.getApplicantDisabilitiesDTO(ID);
        when(applicantDisabilitiesService.create(any()))
                .thenReturn(applicantDisabilitiesDTO);
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantDisabilitiesDTO(ID)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(applicantDisabilitiesService).create(applicantDisabilitiesDTO);

    }

    @Test
    void givenValidRequest_whenDeleteApplicantDisabilitiesIsInvoked_thenDeleteIsSuccess() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL + "/" + ID))
                .andExpect(status().isOk());
        verify(applicantDisabilitiesService).delete(ID);
    }
}
