package gov.uk.courtdata.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.common.dto.ErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public abstract class MockMvcIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    public MvcResult runSuccessScenario(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    }

    public <T> boolean runSuccessScenario(T expectedResponseBody, MockHttpServletRequestBuilder request) throws Exception {
        MvcResult result = runSuccessScenario(request);
        return result.getResponse().getContentAsString().equals(objectMapper.writeValueAsString(expectedResponseBody));
    }

    public boolean runBadRequestErrorScenario(String expectedErrorMessage, MockHttpServletRequestBuilder request) throws Exception {
        ErrorDTO expectedError = ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.name()).message(expectedErrorMessage).build();
        return runErrorScenario(expectedError, request, status().isBadRequest());
    }

    public boolean runNotFoundErrorScenario(String expectedErrorMessage, MockHttpServletRequestBuilder request) throws Exception {
        ErrorDTO expectedError = ErrorDTO.builder().code(HttpStatus.NOT_FOUND.name()).message(expectedErrorMessage).build();
        return runErrorScenario(expectedError, request, status().isNotFound());
    }
    
    public boolean runServerErrorScenario(String expectedErrorMessage, MockHttpServletRequestBuilder request) throws Exception {
        ErrorDTO expectedError = ErrorDTO.builder().message(expectedErrorMessage).build();
        return runErrorScenario(expectedError, request, status().is5xxServerError());
    }

    private boolean runErrorScenario(ErrorDTO expectedError, MockHttpServletRequestBuilder request, ResultMatcher resultMatcher) throws Exception {
        MvcResult result = mockMvc.perform(request).andExpect(resultMatcher).andReturn();
        return result.getResponse().getContentAsString().equals(objectMapper.writeValueAsString(expectedError));
    }
}
