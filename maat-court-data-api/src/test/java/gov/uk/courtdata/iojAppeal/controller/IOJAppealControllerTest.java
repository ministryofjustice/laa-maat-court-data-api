package gov.uk.courtdata.iojAppeal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.iojAppeal.service.IOJAppealService;
import gov.uk.courtdata.iojAppeal.validator.IOJAppealValidationProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static gov.uk.courtdata.builder.TestModelDataBuilder.IOJ_APPEAL_ID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(IOJAppealController.class)
public class IOJAppealControllerTest {

    private static final boolean IS_VALID = true;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IOJAppealService iojAppealService;

    @MockBean
    private IOJAppealValidationProcessor iojAppealValidationProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenValidIOJAppealID_whenGetIOJAppealIsCalled_thenReturnValidIOJAppealResponse_Success() throws Exception {

        when(iojAppealService.find(IOJ_APPEAL_ID)).thenReturn(TestModelDataBuilder.getIOJAppealDTO());
        mvc.perform(MockMvcRequestBuilders.get("/ioj-appeal/"+IOJ_APPEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IOJ_APPEAL_ID));
    }

    @Test
    public void givenNonExistentIOJAppealID_WhenGetIOJAppealIsCalled_ThenReturnBadRequestResponse_Error() throws Exception {
        when(iojAppealService.find(IOJ_APPEAL_ID)).thenThrow( new RequestedObjectNotFoundException("No IOJAppeal object found. ID: "+IOJ_APPEAL_ID));
        mvc.perform(MockMvcRequestBuilders.get("/ioj-appeal/"+IOJ_APPEAL_ID))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("No IOJAppeal object found. ID: "+IOJ_APPEAL_ID));
    }

    @Test
    public void givenValidCreateIOJAppeal_whenCreateIOJAppealIsCalled_thenReturnCreatedIOJAppealObjectWithID() throws Exception {
        //given
        var createIOJAppeal= TestModelDataBuilder.getCreateIOJAppealObject();
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();

        when(iojAppealService.create(createIOJAppeal)).thenReturn(iojAppealDTO);

        var createIOJAppealJson = objectMapper.writeValueAsString(createIOJAppeal);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.post("/ioj-appeal").content(createIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IOJ_APPEAL_ID));
    }

    @Test
    public void givenInvalidCreateIOJAppeal_whenCreateIOJAppealIsCalled_thenFailRequestParameterSchemaValidation() throws Exception {
        //given
        var createIOJAppeal= TestModelDataBuilder.getCreateIOJAppealObject(!IS_VALID);

        var createIOJAppealJson = objectMapper.writeValueAsString(createIOJAppeal);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.post("/ioj-appeal").content(createIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void createIOJAppeal_ServerError_RequestBodyIsMissing() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/ioj-appeal").content("").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void createIOJAppeal_BadRequest_RequestEmptyBody() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/ioj-appeal").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenUpdateIOJAppeal_whenUpdateOJAppealIsCalled_thenReturnUpdatedIOJAppeal() throws Exception {
        //given
        var updateIOJAppeal= TestModelDataBuilder.getUpdateIOJAppealObject();
        var updatedIOJAppealDTO = TestModelDataBuilder.getIOJAppealDTO();

        when(iojAppealValidationProcessor.validate(updateIOJAppeal)).thenReturn(Optional.empty());
        when(iojAppealService.update(updateIOJAppeal)).thenReturn(updatedIOJAppealDTO);

        var updateIOJAppealJson = objectMapper.writeValueAsString(updateIOJAppeal);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.put("/ioj-appeal").content(updateIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IOJ_APPEAL_ID));
    }

    @Test
    public void givenInvalidUpdateIOJAppeal_whenUpdateOJAppealIsCalled_thenFailRequestParameterSchemaValidation() throws Exception {
        //given
        var updateIOJAppeal= TestModelDataBuilder.getUpdateIOJAppealObject(false);
        var updateIOJAppealJson = objectMapper.writeValueAsString(updateIOJAppeal);

        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.put("/ioj-appeal").content(updateIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenUpdateIOJAppeal_whenUpdateOJAppealIsCalled_AndValidationFails_thenReturnError() throws Exception {
        //given
        var updateIOJAppeal= TestModelDataBuilder.getUpdateIOJAppealObject();

        when(iojAppealValidationProcessor.validate(updateIOJAppeal)).thenThrow(new ValidationException("IOJ Appeal does not exist in Db. ID: " + updateIOJAppeal.getId()));

        var updateIOJAppealJson = objectMapper.writeValueAsString(updateIOJAppeal);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.put("/ioj-appeal").content(updateIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("IOJ Appeal does not exist in Db. ID: " + updateIOJAppeal.getId()));
    }
}