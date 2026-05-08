package gov.uk.courtdata.billing.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.billing.request.UpdateBillingRequest;
import gov.uk.courtdata.billing.service.ApplicantHistoryBillingService;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.MAATCourtDataException;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ApplicantHistoryBillingController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc(addFilters = false)
class ApplicantHistoryBillingControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/billing/applicant-history";
    private static final String EXCEPTION_MSG = "Test exception message.";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ApplicantHistoryBillingService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenApplicantHistoryBillingDataPresent_whenGetApplicantHistoryIsCalled_thenOkResponseWithData()
            throws Exception {
        when(service.extractApplicantHistory())
                .thenReturn(List.of(TestModelDataBuilder.getApplicantHistoryBillingDTO()));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].applId").value(716));
    }

    @Test
    void givenNoApplicantHistoryBillingDataPresent_whenGetApplicantHistoryIsCalled_thenInternalServerErrorResponse()
            throws Exception {
        when(service.extractApplicantHistory()).thenThrow(new QueryTimeoutException(EXCEPTION_MSG));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(EXCEPTION_MSG));
    }

    @Test
    void givenValidRequest_whenResetApplicantHistoryIsCalled_thenOkResponse() throws Exception {
        UpdateBillingRequest request = TestModelDataBuilder.getUpdateBillingRequest();

        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("gov.uk.courtdata.builder.TestModelDataBuilder#getUpdateBillingRequests")
    void givenInvalidRequest_whenResetApplicantHistoryIsCalled_thenBadRequestResponse(UpdateBillingRequest request)
            throws Exception {
        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenIncorrectNumberOfRowsUpdated_whenResetApplicantHistoryIsCalled_thenInternalServerErrorResponse()
            throws Exception {
        UpdateBillingRequest request = TestModelDataBuilder.getUpdateBillingRequest();
        doThrow(new MAATCourtDataException(EXCEPTION_MSG)).when(service).resetApplicantHistory(request);

        mvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(EXCEPTION_MSG));
    }
}
