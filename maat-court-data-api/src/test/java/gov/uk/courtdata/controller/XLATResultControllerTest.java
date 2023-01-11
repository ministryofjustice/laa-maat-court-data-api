package gov.uk.courtdata.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.service.ResultsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(XLATResultController.class)
public class XLATResultControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/xlat-result";
    private static final Integer TEST_WQ_TYPE = 5555;
    private static final Integer TEST_SUB_TYPE = 666;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ResultsService resultsService;

    @Test
    void givenIncorrectParameters_whenGetResultCodesByWqTypeAndSubTypeIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/caseId/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenGetResultCodesByWqTypeAndSubTypeIsInvoked_thenReturnResultCode() throws Exception {
        when(resultsService.findXLATResultCodesByWQTypeAndSubTypeCode(TEST_WQ_TYPE, TEST_SUB_TYPE))
                .thenReturn(List.of(TestModelDataBuilder.TEST_RESULT_CODE));
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/wqType/" + TEST_WQ_TYPE + "/subType/" + TEST_SUB_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value(TestModelDataBuilder.TEST_RESULT_CODE));
    }

    @Test
    void givenAValidParameters_whenGetResultCodesForCCBenchWarrantIsInvoked_thenReturnResultCode() throws Exception {
        when(resultsService.findXLATResultCodesForCCBenchWarrant())
                .thenReturn(List.of(TestModelDataBuilder.TEST_RESULT_CODE));
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/cc-bench-warrant"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value(TestModelDataBuilder.TEST_RESULT_CODE));
    }

    @Test
    void givenAValidParameters_whenGetResultCodesForCCImprisonmentIsInvoked_thenReturnResultCode() throws Exception {
        when(resultsService.findXLATResultCodesForCCImprisonment())
                .thenReturn(List.of(TestModelDataBuilder.TEST_RESULT_CODE));
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/cc-imprisonment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value(TestModelDataBuilder.TEST_RESULT_CODE));
    }

}