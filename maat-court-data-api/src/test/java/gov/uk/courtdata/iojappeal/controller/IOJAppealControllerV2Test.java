package gov.uk.courtdata.iojappeal.controller;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.LEGACY_IOJ_APPEAL_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.iojappeal.service.IOJAppealService;
import gov.uk.courtdata.iojappeal.validator.IOJAppealValidationProcessor;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(IOJAppealControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
class IOJAppealControllerV2Test {

    private static final boolean IS_VALID = true;
    private static final int INVALID_REP_ID = 3456;
    private static final String ENDPOINT_URL = "/api/internal/v2/assessment/ioj-appeals";
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private IOJAppealService iojAppealService;
    @MockitoBean
    private IOJAppealValidationProcessor iojAppealValidationProcessor;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidLegacyIojAppealID_whenGetIOJAppealIsCalled_thenReturnValidIOJAppealResponse() throws Exception {
        when(iojAppealService.findByLegacyAppealId(LEGACY_IOJ_APPEAL_ID)).thenReturn(TestModelDataBuilder.getApiGetIojAppealResponse());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_IOJ_APPEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.legacyAppealId").value(LEGACY_IOJ_APPEAL_ID));
    }

    @Test
    void givenNonExistentIOJAppealID_WhenGetIOJAppealIsCalled_ThenReturnBadRequestResponse_Error() throws Exception {
        when(iojAppealService.findByLegacyAppealId(LEGACY_IOJ_APPEAL_ID)).thenThrow(new RequestedObjectNotFoundException("No IOJAppeal object found. ID: " + LEGACY_IOJ_APPEAL_ID));
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + LEGACY_IOJ_APPEAL_ID))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("No IOJAppeal object found. ID: " + LEGACY_IOJ_APPEAL_ID));
    }

    @Test
    void givenValidCreateIOJAppeal_whenCreateIOJAppealIsCalled_thenReturnCreatedIOJAppealObjectWithID() throws Exception {
        //given
        var apiCreateIojAppealRequest = TestModelDataBuilder.getApiCreateIojAppealRequest();
        var apiCreateIojAppealResponse = TestModelDataBuilder.getApiCreateIojAppealResponse();

        when(iojAppealService.create(apiCreateIojAppealRequest)).thenReturn(apiCreateIojAppealResponse);

        var createIOJAppealJson = objectMapper.writeValueAsString(apiCreateIojAppealRequest);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content(createIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.legacyAppealId").value(LEGACY_IOJ_APPEAL_ID));
    }

    @Test
    void givenInvalidCreateIOJAppeal_whenCreateIOJAppealIsCalled_thenFailRequestParameterSchemaValidation() throws Exception {
        //given
        var createIOJAppeal = TestModelDataBuilder.getCreateIOJAppealObject(!IS_VALID);

        var createIOJAppealJson = objectMapper.writeValueAsString(createIOJAppeal);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content(createIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createIOJAppeal_ServerError_RequestBodyIsMissing() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content("").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createIOJAppeal_BadRequest_RequestEmptyBody() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}