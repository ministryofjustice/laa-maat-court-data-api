package gov.uk.courtdata.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.service.ResultsService;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ResultController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ResultControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/result";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ResultsService resultsService;

    @Test
    void givenIncorrectParameters_whenGetResultCodeByCaseIdAndAsnSeqIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/caseId/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenGetResultCodeByCaseIdAndAsnSeqIsInvoked_thenReturnResultCode() throws Exception {
        when(resultsService.findResultCodesByCaseIdAndAsnSeq(
                        TestModelDataBuilder.TEST_CASE_ID, TestModelDataBuilder.TEST_ASN_SEQ))
                .thenReturn(List.of(TestModelDataBuilder.TEST_RESULT_CODE));
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/caseId/" + TestModelDataBuilder.TEST_CASE_ID
                        + "/asnSeq/" + TestModelDataBuilder.TEST_ASN_SEQ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value(TestModelDataBuilder.TEST_RESULT_CODE));
    }
}
