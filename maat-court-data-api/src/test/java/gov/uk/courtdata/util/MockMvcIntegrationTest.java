package gov.uk.courtdata.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.dto.ErrorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public abstract class MockMvcIntegrationTest {

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected WebApplicationContext webApplicationContext;

    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    public MvcResult runSuccessScenario(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    }

    public <T> void runSuccessScenario(T expectedResponseBody, MockHttpServletRequestBuilder request) throws Exception {
        MvcResult result = runSuccessScenario(request);

        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(expectedResponseBody));
    }

    public void runBadRequestErrorScenario(String expectedErrorMessage, MockHttpServletRequestBuilder request) throws Exception {
        ErrorDTO expectedError = ErrorDTO.builder().code(HttpStatus.BAD_REQUEST.name()).message(expectedErrorMessage).build();
        runErrorScenario(expectedError, request, status().isBadRequest());
    }

    public void runNotFoundErrorScenario(String expectedErrorMessage, MockHttpServletRequestBuilder request) throws Exception {
        ErrorDTO expectedError = ErrorDTO.builder().code(HttpStatus.NOT_FOUND.name()).message(expectedErrorMessage).build();
        runErrorScenario(expectedError, request, status().isNotFound());
    }
    
    public void runServerErrorScenario(String expectedErrorMessage, MockHttpServletRequestBuilder request) throws Exception {
        ErrorDTO expectedError = ErrorDTO.builder().message(expectedErrorMessage).build();
        runErrorScenario(expectedError, request, status().is5xxServerError());
    }

    private void runErrorScenario(ErrorDTO expectedError, MockHttpServletRequestBuilder request, ResultMatcher resultMatcher) throws Exception {
        MvcResult result = mockMvc.perform(request).andExpect(resultMatcher).andReturn();
        assertThat(result.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(expectedError));
    }
}
