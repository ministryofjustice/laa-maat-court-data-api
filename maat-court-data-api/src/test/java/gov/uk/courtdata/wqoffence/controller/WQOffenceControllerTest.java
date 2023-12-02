package gov.uk.courtdata.wqoffence.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.wqoffence.service.WQOffenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WQOffenceController.class)
@AutoConfigureMockMvc(addFilters = false)
class WQOffenceControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private WQOffenceService wqOffenceService;

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/wq-offence";

    @Test
    void givenIncorrectParameters_whenGetNewOffenceCountIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/case/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenGetNewOffenceCountIsInvoked_thenReturnNewOffenceCount() throws Exception {
        when(wqOffenceService.getNewOffenceCount(TestModelDataBuilder.TEST_CASE_ID, TestModelDataBuilder.TEST_OFFENCE_ID)).thenReturn(1);
        mvc.perform(MockMvcRequestBuilders.head(ENDPOINT_URL + "/" + TestModelDataBuilder.TEST_OFFENCE_ID + "/case/" + TestModelDataBuilder.TEST_CASE_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "1"));
    }

}