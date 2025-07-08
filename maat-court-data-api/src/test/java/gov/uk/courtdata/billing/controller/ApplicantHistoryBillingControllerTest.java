package gov.uk.courtdata.billing.controller;

import gov.uk.courtdata.billing.service.ApplicantHistoryBillingService;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    void givenApplicantHistoryBillingDataPresent_whenGetApplicantHistoryIsCalled_thenOkResponseWithData() throws Exception {
        when(service.extractApplicantHistory())
                .thenReturn(List.of(TestModelDataBuilder.getApplicantHistoryBillingDTO()));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].applId").value(716));
    }

    @Test
    void givenNoApplicantHistoryBillingDataPresent_whenGetApplicantHistoryIsCalled_thenInternalServerErrorResponse() throws Exception {
        when(service.extractApplicantHistory()).thenThrow(new QueryTimeoutException(EXCEPTION_MSG));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(EXCEPTION_MSG));
    }
}
