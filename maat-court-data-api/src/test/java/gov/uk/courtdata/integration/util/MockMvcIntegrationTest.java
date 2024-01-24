package gov.uk.courtdata.integration.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import gov.uk.courtdata.dto.ErrorDTO;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@Slf4j
public abstract class MockMvcIntegrationTest {

    private static final int WIREMOCK_PORT;

    static {
        try (var serverSocket = new ServerSocket(0)) {
            WIREMOCK_PORT = serverSocket.getLocalPort();
        } catch (IOException e) {
            String message = "Unable to bind to a free port: [%s]".formatted(e.getMessage());
            throw new RuntimeException(message, e);
        }
    }

    private static WireMockServer wireMockServer;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected Repositories repos;

    @DynamicPropertySource
    static void configureDynamicWireMockPort(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.client.provider.cda.token-uri", () -> "http://localhost:" + WIREMOCK_PORT + "/oauth2/token");
        registry.add("cda.url", () -> "http://localhost:" + WIREMOCK_PORT + "/");
    }

    @BeforeAll
    static void beforeAll() {
        WireMock.configureFor(WIREMOCK_PORT);

        WireMockConfiguration wireMockServerConfig = WireMockConfiguration.options().port(WIREMOCK_PORT);
        wireMockServer = new WireMockServer(wireMockServerConfig);
        wireMockServer.start();
        waitUntil(() -> wireMockServer.isRunning());
    }

    @AfterAll
    static void afterAll() {
        wireMockServer.shutdown();
        waitUntil(() -> !wireMockServer.isRunning());
    }

    private static void waitUntil(Supplier<Boolean> isComplete) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        while (!isComplete.get() &&
                stopWatch.getTime(TimeUnit.SECONDS) < 3) {
            // Loop until complete or timeout is reached
        }
        stopWatch.reset();
    }

    @BeforeEach
    void resetWireMockAndClearAllRepositoriesBeforeEach() {
        configureObjectMapper(objectMapper);
        wireMockServer.resetAll();
        repos.clearAll();
        repos.insertCommonTestData();
    }

    @AfterEach
    void resetWireMockAndClearAllRepositoriesAfterEach() {
        configureObjectMapper(objectMapper);
        repos.clearAll();
        wireMockServer.resetAll();
    }

    private void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
    }

    protected static WireMockServer wireMock() {
        return wireMockServer;
    }

    public MvcResult runSuccessScenario(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    }

    public <T> boolean runSuccessScenario(T expectedResponseBody, MockHttpServletRequestBuilder request) throws Exception {
        MvcResult result = runSuccessScenario(request);
        String expectedJson = objectMapper.writeValueAsString(expectedResponseBody);
        String actualJson = result.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedJson,
                actualJson,
                JSONCompareMode.LENIENT);
        return true;
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

    private boolean runErrorScenario(ErrorDTO expectedError,
                                     MockHttpServletRequestBuilder request,
                                     ResultMatcher resultMatcher) throws Exception {
        MvcResult result = mockMvc.perform(request)
                .andExpect(resultMatcher)
                .andReturn();

        JSONAssert.assertEquals(objectMapper.writeValueAsString(expectedError),
                result.getResponse().getContentAsString(),
                JSONCompareMode.STRICT);

        return true;
    }
}
