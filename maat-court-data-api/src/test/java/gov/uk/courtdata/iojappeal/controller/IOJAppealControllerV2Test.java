package gov.uk.courtdata.iojappeal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.iojappeal.service.IOJAppealV2Service;
import gov.uk.courtdata.iojappeal.validator.ApiCreateIojAppealRequestValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;
import uk.gov.justice.laa.crime.error.ErrorExtension;
import uk.gov.justice.laa.crime.error.ProblemDetailError;
import uk.gov.justice.laa.crime.util.ProblemDetailUtil;

import java.util.Optional;

import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_IOJ_APPEAL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IOJAppealControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class IOJAppealControllerV2Test {

    private static final String ENDPOINT_URL = "/api/internal/v2/assessment/ioj-appeals";
    private static final String ROLLBACK_URL = ENDPOINT_URL + "/rollback/{iojAppealId}";

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private IOJAppealV2Service iojAppealService;

    @MockitoBean
    private ApiCreateIojAppealRequestValidator apiCreateIojAppealRequestValidator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidLegacyIojAppealId_whenGetIojAppealIsCalled_thenOkResponseIsReturned()
            throws Exception {
        when(iojAppealService.find(LEGACY_IOJ_APPEAL_ID)).thenReturn(
                TestModelDataBuilder.getApiGetIojAppealResponse());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_IOJ_APPEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.legacyAppealId").value(LEGACY_IOJ_APPEAL_ID));

        verify(iojAppealService).find(LEGACY_IOJ_APPEAL_ID);
    }

    @Test
    void givenNonExistentAppeal_whenGetIOJAppealIsCalled_thenNotFoundResponseIsReturned()
            throws Exception {
        when(iojAppealService.find(LEGACY_IOJ_APPEAL_ID)).thenThrow(
                new RequestedObjectNotFoundException(
                        "No IoJ Appeal found for ID: " + LEGACY_IOJ_APPEAL_ID));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_IOJ_APPEAL_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();

        validateProblemDetailResponse(result.getResponse().getContentAsString(),
                ProblemDetailError.OBJECT_NOT_FOUND.code(),
                "No IoJ Appeal found for ID: " + LEGACY_IOJ_APPEAL_ID);
        verify(iojAppealService).find(LEGACY_IOJ_APPEAL_ID);
    }

    @Test
    void givenValidCreateIojAppealRequest_whenCreateIojAppealIsCalled_thenOkResponseIsReturned()
            throws Exception {
        var apiCreateIojAppealResponse = TestModelDataBuilder.getApiCreateIojAppealResponse();

        when(iojAppealService.create(any(ApiCreateIojAppealRequest.class)))
                .thenReturn(apiCreateIojAppealResponse);

        var apiCreateIojAppealRequest = TestModelDataBuilder.getApiCreateIojAppealRequest();
        var createIOJAppealJson = objectMapper.writeValueAsString(apiCreateIojAppealRequest);

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(createIOJAppealJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.legacyAppealId").value(LEGACY_IOJ_APPEAL_ID));

        verify(iojAppealService).create(any(ApiCreateIojAppealRequest.class));
    }

    @Test
    void givenInvalidCreateIOJAppeal_whenCreateIOJAppealIsCalled_thenBadRequestIsReturned()
            throws Exception {
        var createIOJAppeal = TestModelDataBuilder.getApiCreateIojAppealRequest();
        createIOJAppeal.getIojAppealMetadata().setLegacyApplicationId(null);
        var createIOJAppealJson = objectMapper.writeValueAsString(createIOJAppeal);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(createIOJAppealJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        validateProblemDetailResponse(result.getResponse().getContentAsString(),
                ProblemDetailError.VALIDATION_FAILURE.code(),
                ProblemDetailError.VALIDATION_FAILURE.defaultDetail());
    }

    @Test
    void givenMissingRequestBody_whenCreateIojAppealIsCalled_thenBadRequestIsReturned()
            throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        validateProblemDetailResponse(result.getResponse().getContentAsString(),
                ProblemDetailError.BAD_REQUEST.code(),
                ProblemDetailError.BAD_REQUEST.defaultDetail());
    }

    @Test
    void givenValidIoJAppealId_whenRollbackIoJAppealIsCalled_thenOKResponseIsReturned()
            throws Exception {
        doNothing().when(iojAppealService).rollback(LEGACY_IOJ_APPEAL_ID);

        mvc.perform(MockMvcRequestBuilders.patch(ROLLBACK_URL, LEGACY_IOJ_APPEAL_ID))
                .andExpect(status().isOk());
    }

    @Test
    void givenAnInvalidIoJAppealId_whenRollbackIoJAppealIsCalled_thenNotFoundIsReturned()
            throws Exception {
        String exceptionMessage = String.format("No IOJ Appeal found for ID: %d",
                LEGACY_IOJ_APPEAL_ID);

        doThrow(new RequestedObjectNotFoundException(exceptionMessage)).when(iojAppealService)
                .rollback(LEGACY_IOJ_APPEAL_ID);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.patch(ROLLBACK_URL, LEGACY_IOJ_APPEAL_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andReturn();

        validateProblemDetailResponse(result.getResponse().getContentAsString(),
                ProblemDetailError.OBJECT_NOT_FOUND.code(),
                "No IOJ Appeal found for ID: " + LEGACY_IOJ_APPEAL_ID);
    }

    private void validateProblemDetailResponse(String responseString, String expectedCode, String expectedDetail) throws JsonProcessingException {
        ProblemDetail problemDetail = ProblemDetailUtil.parseProblemDetailJson(responseString);
        assertThat(problemDetail.getDetail()).isEqualTo(expectedDetail);
        Optional<ErrorExtension> extension = ProblemDetailUtil.getErrorExtension(problemDetail);
        assertThat(extension).isPresent().get().hasFieldOrPropertyWithValue("code", expectedCode);
    }
}