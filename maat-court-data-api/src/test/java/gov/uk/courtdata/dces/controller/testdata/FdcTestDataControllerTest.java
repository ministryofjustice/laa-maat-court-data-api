package gov.uk.courtdata.dces.controller.testdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.dces.request.CreateFdcTestDataRequest;
import gov.uk.courtdata.dces.request.FdcNegativeTestType;
import gov.uk.courtdata.dces.service.FdcContributionsTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnabledIf(expression = "#{environment['sentry.environment'] == 'development'}", loadContext = true)
@WebMvcTest(FdcTestDataController.class)
@AutoConfigureMockMvc(addFilters = false)
class FdcTestDataControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/debt-collection-enforcement/test-data";

    @Autowired
    private MockMvc mvc;

    final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private FdcContributionsTestService fdcContributionsTestService;

    @Test
    void testNonErrorMapping() throws Exception {
        boolean isNegativeTest = false;
        FdcNegativeTestType testType = null;
        Integer numRows = 5;

        CreateFdcTestDataRequest request = buildRequest(numRows, isNegativeTest, testType);
        String requestBody = getRequestString(request);
        when(fdcContributionsTestService.createFdcMergeTestData(request))
                .thenReturn(new ArrayList<>());

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/generate_prepare_fdc_data_1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void testNegativeTypeValuesMapping() throws Exception {
        boolean isNegativeTest = true;
        FdcNegativeTestType testType = FdcNegativeTestType.CCO;
        Integer numRows = 5;

        CreateFdcTestDataRequest request = buildRequest(numRows, isNegativeTest, testType);
        String requestBody = getRequestString(request);
        when(fdcContributionsTestService.createFdcMergeTestData(request))
                .thenReturn(new ArrayList<>());

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/generate_prepare_fdc_data_1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    void testInvalidNegativeType() throws Exception {
        boolean isNegativeTest = false;
        FdcNegativeTestType testType = FdcNegativeTestType.SOD;
        Integer numRows = 5;

        CreateFdcTestDataRequest request = buildRequest(numRows, isNegativeTest, testType);
        String requestBody = getRequestString(request);
        requestBody = requestBody.replace("SOD", "INVALID");
        when(fdcContributionsTestService.createFdcMergeTestData(any()))
                .thenReturn(new ArrayList<>());

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/generate_prepare_fdc_data_1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNoNegativeTypeSpecified() throws Exception {
        boolean isNegativeTest = true;
        FdcNegativeTestType testType = null;
        Integer numRows = 5;

        CreateFdcTestDataRequest request = buildRequest(numRows, isNegativeTest, testType);
        String requestBody = getRequestString(request);
        when(fdcContributionsTestService.createFdcMergeTestData(request))
                .thenReturn(new ArrayList<>());

        mvc.perform(MockMvcRequestBuilders.post(String.format(ENDPOINT_URL  +"/generate_prepare_fdc_data_1"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }


    private CreateFdcTestDataRequest buildRequest(Integer numTestEntries, boolean isNegative, FdcNegativeTestType negativeType){
        return CreateFdcTestDataRequest.builder()
                .numOfTestEntries(numTestEntries)
                .negativeTest(isNegative)
                .negativeTestType(negativeType)
                .build();
    }

    private String getRequestString(CreateFdcTestDataRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request);
    }

}