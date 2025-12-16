package gov.uk.courtdata.iojappeal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.IOJAppealDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.iojappeal.service.IOJAppealV1Service;
import gov.uk.courtdata.iojappeal.validator.IOJAppealValidationProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_REP_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IOJAppealController.class)
@AutoConfigureMockMvc(addFilters = false)
class IOJAppealControllerTest {

    private static final boolean IS_VALID = true;
    private static final int INVALID_REP_ID = 3456;
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/ioj-appeal";
    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private IOJAppealV1Service iojAppealService;
    @MockitoBean
    private IOJAppealValidationProcessor iojAppealValidationProcessor;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidIOJAppealID_whenGetIOJAppealIsCalled_thenReturnValidIOJAppealResponse_Success() throws Exception {
        when(iojAppealService.find(IOJ_APPEAL_ID)).thenReturn(TestModelDataBuilder.getIOJAppealDTO());
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + IOJ_APPEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IOJ_APPEAL_ID));
    }

    @Test
    void givenNonExistentIOJAppealID_WhenGetIOJAppealIsCalled_ThenReturnBadRequestResponse_Error() throws Exception {
        when(iojAppealService.find(IOJ_APPEAL_ID)).thenThrow(new RequestedObjectNotFoundException("No IoJAppeal object found. ID: " + IOJ_APPEAL_ID));
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + IOJ_APPEAL_ID))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("No IoJAppeal object found. ID: " + IOJ_APPEAL_ID));
    }

    @Test
    void givenCorrectParameters_whenGetIOJAppealByRepIdIsInvoked_thenIOJAppealIsReturned() throws Exception {
        IOJAppealDTO iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        when(iojAppealService.findByRepId(IOJ_REP_ID)).thenReturn(iojAppealDTO);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + IOJ_REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(IOJ_APPEAL_ID)))
                .andExpect(jsonPath("$.repId").value(String.valueOf(IOJ_REP_ID)));

        verify(iojAppealService).findByRepId(IOJ_REP_ID);
    }

    @Test
    void givenInvalidRepId_whenGetIOJAppealByRepIdIsInvoked_then404NotFoundErrorIsThrown() throws Exception {
        when(iojAppealService.findByRepId(INVALID_REP_ID))
                .thenThrow(new RequestedObjectNotFoundException(String.format("No IoJAppeal object found for repId %s", INVALID_REP_ID)));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + INVALID_REP_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenCorrectParameters_whenGetCurrentPassedIOJAppealByRepIdIsCalled_thenReturnValidIOJAppealResponse_Success() throws Exception {
        IOJAppealDTO iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        when(iojAppealService.findCurrentPassedAppealByRepId(IOJ_REP_ID)).thenReturn(iojAppealDTO);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + IOJ_REP_ID + "/current-passed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(String.valueOf(IOJ_APPEAL_ID)))
                .andExpect(jsonPath("$.repId").value(String.valueOf(IOJ_REP_ID)));

        verify(iojAppealService).findCurrentPassedAppealByRepId(IOJ_REP_ID);
    }

    @Test
    void givenInvalidRepId_whenGetCurrentPassedIOJAppealByRepIdIsCalled_then404NotFoundErrorIsThrown() throws Exception {
        when(iojAppealService.findCurrentPassedAppealByRepId(INVALID_REP_ID))
                .thenThrow(new RequestedObjectNotFoundException(String.format("No IoJAppeal object found for repId %s", INVALID_REP_ID)));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + INVALID_REP_ID + "/current-passed"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNullRepId_whenGetIOJAppealByRepIdIsInvoked_thenBadRequestIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + "/repId/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidCreateIOJAppeal_whenCreateIOJAppealIsCalled_thenReturnCreatedIOJAppealObjectWithID() throws Exception {
        //given
        var createIOJAppeal = TestModelDataBuilder.getCreateIOJAppealObject();
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();

        when(iojAppealService.create(createIOJAppeal)).thenReturn(iojAppealDTO);

        var createIOJAppealJson = objectMapper.writeValueAsString(createIOJAppeal);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL).content(createIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IOJ_APPEAL_ID));
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

    @Test
    void givenUpdateIOJAppeal_whenUpdateOJAppealIsCalled_thenReturnUpdatedIOJAppeal() throws Exception {
        //given
        var updateIOJAppeal = TestModelDataBuilder.getUpdateIOJAppealObject();
        var updatedIOJAppealDTO = TestModelDataBuilder.getIOJAppealDTO();

        when(iojAppealValidationProcessor.validate(updateIOJAppeal)).thenReturn(Optional.empty());
        when(iojAppealService.update(updateIOJAppeal)).thenReturn(updatedIOJAppealDTO);

        var updateIOJAppealJson = objectMapper.writeValueAsString(updateIOJAppeal);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content(updateIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IOJ_APPEAL_ID));
    }

    @Test
    void givenInvalidUpdateIOJAppeal_whenUpdateOJAppealIsCalled_thenFailRequestParameterSchemaValidation() throws Exception {
        //given
        var updateIOJAppeal = TestModelDataBuilder.getUpdateIOJAppealObject(false);
        var updateIOJAppealJson = objectMapper.writeValueAsString(updateIOJAppeal);

        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content(updateIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenUpdateIOJAppeal_whenUpdateOJAppealIsCalled_AndValidationFails_thenReturnError() throws Exception {
        //given
        var updateIOJAppeal = TestModelDataBuilder.getUpdateIOJAppealObject();

        when(iojAppealValidationProcessor.validate(updateIOJAppeal)).thenThrow(new ValidationException("IoJ Appeal does not exist in Db. ID: " + updateIOJAppeal.getId()));

        var updateIOJAppealJson = objectMapper.writeValueAsString(updateIOJAppeal);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content(updateIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("IoJ Appeal does not exist in Db. ID: " + updateIOJAppeal.getId()));
    }
}