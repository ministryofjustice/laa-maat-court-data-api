package gov.uk.courtdata.billing.controller;

import gov.uk.courtdata.billing.service.ApplicantCclfResetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicantCclfResetController.class)
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ApplicantCclfResetControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/billing/applicant-cclf-reset";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ApplicantCclfResetService applicantCclfResetService;

    @Test
    void givenRequest_whenResetCclfFlag_thenReturnNoContent() throws Exception {
        doNothing().when(applicantCclfResetService).resetApplicantCclfFlag();

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL))
                .andExpect(status().isNoContent());

        verify(applicantCclfResetService).resetApplicantCclfFlag();
    }

    @Test
    void givenRequest_whenServiceThrowsException_thenReturnInternalServerError() throws Exception {
        doThrow(new RuntimeException("Simulated DB failure"))
                .when(applicantCclfResetService).resetApplicantCclfFlag();

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL))
                .andExpect(status().isInternalServerError());

        verify(applicantCclfResetService).resetApplicantCclfFlag();
    }
}