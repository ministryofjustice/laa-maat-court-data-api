package gov.uk.courtdata.billing.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import gov.uk.courtdata.billing.entity.BillingApplicantEntity;
import gov.uk.courtdata.billing.service.BillingApplicantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(BillingApplicantController.class)
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BillingApplicantControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/billing/applicants";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private BillingApplicantService billingApplicantService;

    @Test
    void givenNoInput_whenGetApplicantsToBill_thenResponseIsReturned() throws Exception {
        List<BillingApplicantEntity> applicants = new ArrayList<>();
        BillingApplicantEntity applicant = new BillingApplicantEntity();
        applicants.add(applicant);
        when(billingApplicantService.findAllApplicantsForBilling()).thenReturn(applicants);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(billingApplicantService).findAllApplicantsForBilling();
    }

}