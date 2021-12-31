package gov.uk.courtdata.iojAppeal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.builder.TestModelDataBuilder;
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
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
@RunWith(SpringRunner.class)
@WebMvcTest(IOJAppealController.class)
public class IOJAppealControllerTest {

    private static final boolean IS_VALID = true;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IOJAppealValidationProcessor iojAppealValidationProcessor;

    @MockBean
    private IOJAppealService iojAppealService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenValidIOJAppealID_ThenReturnValidIOJAppealResponse_Success() throws Exception {
        when(iojAppealValidationProcessor.validate(IOJ_APPEAL_ID)).thenReturn(Optional.empty());

        when(iojAppealService.find(IOJ_APPEAL_ID)).thenReturn(TestModelDataBuilder.getIOJAppealDTO());
        mvc.perform(MockMvcRequestBuilders.get("/ioj-appeal/"+IOJ_APPEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IOJ_APPEAL_ID));
    }

    @Test
    public void givenInvalidIOJAppealID_ThenReturnExceptionResponse_Error() throws Exception {
        when(iojAppealValidationProcessor.validate(-1)).thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.get("/ioj-appeal/"+-1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void givenValidCreateIOJAppealPassedToPostCall_ThenReturnCreatedIOJAppealObjectWithID() throws Exception {
        //given
        var createIOJAppeal= TestModelDataBuilder.getCreateIOJAppealObject(IS_VALID);
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();
        //when
        //TODO: calling validator process return optional.empty
        when(iojAppealService.create(createIOJAppeal)).thenReturn(iojAppealDTO);

        var createIOJAppealJson = objectMapper.writeValueAsString(createIOJAppeal);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.post("/ioj-appeal").content(createIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(IOJ_APPEAL_ID));
    }

    @Test
    public void givenInvalidCreateIOJAppealPassedToPostCall_ThenReturnError_FailRequestParameterObjectValidation() throws Exception {
        //given
        var createIOJAppeal= TestModelDataBuilder.getCreateIOJAppealObject(!IS_VALID);
        var iojAppealDTO = TestModelDataBuilder.getIOJAppealDTO();

        var createIOJAppealJson = objectMapper.writeValueAsString(createIOJAppeal);
        //on call check the id of the object and any other value
        mvc.perform(MockMvcRequestBuilders.post("/ioj-appeal").content(createIOJAppealJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void createIOJAppeal_ServerError_RequestBodyIsMissing() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/ioj-appeal").content(new String()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
    @Test
    public void createIOJAppeal_BadRequest_RequestEmtpyBody() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/ioj-appeal").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}