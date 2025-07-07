package gov.uk.courtdata.billing.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.billing.request.UpdateRepOrderBillingRequest;
import gov.uk.courtdata.billing.service.RepOrderBillingService;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.MAATCourtDataException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(RepOrderBillingController.class)
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RepOrderBillingControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/billing/rep-orders";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RepOrderBillingService repOrderBillingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidRequest_whenGetRepOrdersForBillingIsInvoked_thenResponseIsReturned() throws Exception {
        when(repOrderBillingService.getRepOrdersForBilling()).thenReturn(List.of(
            TestModelDataBuilder.getRepOrderBillingDTO(123)));

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenInvalidRequest_whenPatchRepOrderForBillingIsInvoked_thenFailureResponseIsReturned() throws Exception {
        UpdateRepOrderBillingRequest request = UpdateRepOrderBillingRequest.builder()
            .userModified(null)
            .repOrderIds(List.of(10034567, 10034568, 10034591))
            .build();

        mockMvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenDownstreamException_whenPatchRepOrderForBillingIsInvoked_thenFailureResponseIsReturned() throws Exception {
        UpdateRepOrderBillingRequest request = UpdateRepOrderBillingRequest.builder()
            .userModified("joe-bloggs")
            .repOrderIds(List.of(10034567, 10034568, 10034591))
            .build();

        doThrow(new MAATCourtDataException("Error")).when(repOrderBillingService).resetRepOrdersSentForBilling(request);

        mockMvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void givenValidRequest_whenPatchRepOrderForBillingIsInvoked_thenSuccessResponseIsReturned() throws Exception {
        UpdateRepOrderBillingRequest request = UpdateRepOrderBillingRequest.builder()
            .userModified("joe-bloggs")
            .repOrderIds(List.of(10034567, 10034568, 10034591))
            .build();

        mockMvc.perform(MockMvcRequestBuilders.patch(ENDPOINT_URL)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());

        verify(repOrderBillingService).resetRepOrdersSentForBilling(request);
    }

}
