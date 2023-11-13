package gov.uk.courtdata.preupdatechecks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.preupdatechecks.dto.ApplicantHistoryDTO;
import gov.uk.courtdata.preupdatechecks.dto.RepOrderApplicantLinksDTO;
import gov.uk.courtdata.preupdatechecks.service.ApplicantHistoryService;
import gov.uk.courtdata.preupdatechecks.service.RepOrderApplicantLinksService;
import gov.uk.courtdata.preupdatechecks.validator.PreUpdateChecksValidationProcessor;
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

@WebMvcTest(PreUpdateChecksController.class)
public class PreUpdateChecksControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/application/pre-update-checks";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ApplicantHistoryService applicantHistoryService;

    @MockBean
    private RepOrderApplicantLinksService repOrderApplicantLinksService;

    @MockBean
    private PreUpdateChecksValidationProcessor validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenIncorrectParameters_whenGetRepOrderApplicantLinksIsInvoked_thenErrorIsThrown() throws Exception {
        when(validator.validate(any())).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/repId/" + REP_ID))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenCorrectRepId_whenGetRepOrderApplicantLinksIsInvoked_thenResponseIsReturned() throws Exception {
        List<RepOrderApplicantLinksDTO> response = TestModelDataBuilder.getRepOrderApplicantLinksDTO();
        when(repOrderApplicantLinksService.getRepOrderApplicantLinks(REP_ID)).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/rep-order-applicant-links/repId/" + REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(response.get(0).getId()))
                .andExpect(jsonPath("$[0].repId").value(response.get(0).getRepId()))
                .andExpect(jsonPath("$[0].linkDate").value(response.get(0).getLinkDate().toString()))
                .andExpect(jsonPath("$[0].unlinkDate").value(response.get(0).getUnlinkDate().toString()));
        verify(repOrderApplicantLinksService).getRepOrderApplicantLinks(REP_ID);
    }

    @Test
    void givenValidRequest_whenUpdateApplicantHistoryIsInvoked_thenUpdateIsSuccess() throws Exception {
        ApplicantHistoryDTO response = TestModelDataBuilder.getApplicantHistoryDTO();
        when(applicantHistoryService.update(any())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL + "/applicant-history")
                .content(objectMapper.writeValueAsString(TestModelDataBuilder.getApplicantHistoryDTO()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.sendToCclf").value(response.getSendToCclf()));
    }

}
