package gov.uk.courtdata.applicant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.applicant.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.applicant.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.applicant.service.ApplicantHistoryService;
import gov.uk.courtdata.applicant.service.RepOrderApplicantLinksService;
import gov.uk.courtdata.applicant.validator.ApplicantValidationProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicantController.class)
public class ApplicantControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/applicant";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ApplicantHistoryService applicantHistoryService;

    @MockBean
    private RepOrderApplicantLinksService repOrderApplicantLinksService;

    @MockBean
    private ApplicantValidationProcessor validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenAValidationException_whenGetRepOrderApplicantLinksIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(validator.validate(any())).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/" + REP_ID))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectRepId_whenGetRepOrderApplicantLinksIsInvoked_thenResponseIsReturned() throws Exception {
        List<RepOrderApplicantLinksDTO> response = TestModelDataBuilder.getRepOrderApplicantLinksDTO();
        when(repOrderApplicantLinksService.getRepOrderApplicantLinks(REP_ID)).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/" + REP_ID))
                .andExpect(status().isOk());
        verify(repOrderApplicantLinksService).getRepOrderApplicantLinks(REP_ID);
    }

    @Test
    void givenValidRequest_whenUpdateApplicantHistoryIsInvoked_thenUpdateIsSuccess() throws Exception {
        ApplicantHistoryDTO applicantHistoryDTO = TestModelDataBuilder.getApplicantHistoryDTO(1, "N");
        when(applicantHistoryService.update(any())).thenReturn(applicantHistoryDTO);
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(applicantHistoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenAEmptyContent_whenUpdateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenInValidRequest_whenUpdateApplicantHistoryIsInvoked_thenCorrectErrorResponseIsReturned() throws Exception {
        when(applicantHistoryService.update(any())).thenThrow(new RequestedObjectNotFoundException("Applicant History not found"));
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                        .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO(1, "Y")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
