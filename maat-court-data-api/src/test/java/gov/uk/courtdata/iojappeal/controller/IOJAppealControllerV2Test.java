package gov.uk.courtdata.iojappeal.controller;

import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_IOJ_APPEAL_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.advice.ProblemDetailError;
import gov.uk.courtdata.iojappeal.service.IOJAppealV2Service;
import gov.uk.courtdata.iojappeal.validator.ApiCreateIojAppealRequestValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.common.model.ioj.ApiCreateIojAppealRequest;

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

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_IOJ_APPEAL_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(
                        jsonPath("$.errors.code").value(ProblemDetailError.OBJECT_NOT_FOUND.code()))
                .andExpect(jsonPath("$.detail").value(
                        "No IoJ Appeal found for ID: " + LEGACY_IOJ_APPEAL_ID));

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

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(createIOJAppealJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.code").value(
                        ProblemDetailError.VALIDATION_FAILURE.code()))
                .andExpect(jsonPath("$.detail").value(
                        ProblemDetailError.VALIDATION_FAILURE.defaultDetail()));
    }

    @Test
    void givenMissingRequestBody_whenCreateIojAppealIsCalled_thenBadRequestIsReturned()
            throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.code").value(ProblemDetailError.BAD_REQUEST.code()))
                .andExpect(
                        jsonPath("$.detail").value(ProblemDetailError.BAD_REQUEST.defaultDetail()));
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

        mvc.perform(MockMvcRequestBuilders.patch(ROLLBACK_URL, LEGACY_IOJ_APPEAL_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(
                        jsonPath("$.errors.code").value(ProblemDetailError.OBJECT_NOT_FOUND.code()))
                .andExpect(jsonPath("$.detail").value(
                        "No IOJ Appeal found for ID: " + LEGACY_IOJ_APPEAL_ID));
    }
}