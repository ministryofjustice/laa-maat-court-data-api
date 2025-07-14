package gov.uk.courtdata.billing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.billing.request.ApplicantResetRequest;
import gov.uk.courtdata.billing.service.ApplicantResetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebMvcTest(ApplicantResetController.class)
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ApplicantResetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ApplicantResetService applicantResetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldResetApplicantSuccessfully() throws Exception {
        ApplicantResetRequest request = new ApplicantResetRequest();
        request.setUsername("test_user");
        request.setApplicantIds(java.util.List.of(1, 2, 3));

        mvc.perform(patch("/api/internal/v1/billing/applicant/reset")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(applicantResetService).resetApplicant(request);
    }
}