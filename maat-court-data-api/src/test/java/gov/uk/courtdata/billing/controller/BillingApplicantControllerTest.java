package gov.uk.courtdata.billing.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.billing.service.BillingApplicantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(BillingApplicantController.class)
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BillingApplicantControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/billing/billing-applicants";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BillingApplicantService billingApplicantService;

    @Test
    void givenNoInput_whenGetApplicantsToBill_thenResponseIsReturned() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL))
                .andExpect(status().isOk());
        verify(billingApplicantService).findAllApplicantsForBilling();
    }

}