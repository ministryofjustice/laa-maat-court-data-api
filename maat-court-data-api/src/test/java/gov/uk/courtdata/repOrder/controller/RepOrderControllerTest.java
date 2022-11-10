package gov.uk.courtdata.repOrder.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.repOrder.service.RepOrderService;
import gov.uk.courtdata.repOrder.validator.UpdateAppDateCompletedValidator;
import gov.uk.courtdata.repOrder.validator.UpdateRepOrderValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RepOrderController.class)
public class RepOrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UpdateAppDateCompletedValidator updateAppDateCompletedValidator;

    @MockBean
    private UpdateRepOrderValidator updateRepOrderValidator;

    @MockBean
    private RepOrderService repOrderService;

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders";

    @Test
    public void givenValidRepId_whenGetRepOrderIsInvoked_thenAssessmentIsRetrieved() throws Exception {
        RepOrderDTO expected = TestModelDataBuilder.getRepOrderDTO();
        when(repOrderService.find(anyInt()))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expected.getId()));
    }

    @Test
    public void givenInvalidRepId_whenGetRepOrderIsInvoked_thenErrorIsThrown() throws Exception {
        when(repOrderService.find(anyInt()))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order found for ID: 1234"));
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("No Rep Order found for ID: 1234"));
    }

    @Test
    public void givenCorrectParameters_whenUpdateAppCompletedIsInvoked_thenCompletedDateShouldUpdated() throws Exception {
        when(updateAppDateCompletedValidator.validate(any(UpdateAppDateCompleted.class)))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/update-date-completed")
                .content(TestModelDataBuilder.getUpdateAppDateCompletedJson())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void givenIncorrectParameters_whenUpdateAppCompletedIsInvoked_thenErrorIsThrown() throws Exception {
        when(updateAppDateCompletedValidator.validate(any(UpdateAppDateCompleted.class)))
                .thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/update-date-completed").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenIncorrectParameters_whenUpdateRepOrderInvoked_thenErrorIsThrown() throws Exception {
        when(updateRepOrderValidator.validate(any(UpdateRepOrder.class)))
                .thenThrow(new ValidationException());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenCorrectParameters_whenUpdateRepOrderInvoked_thenUpdateRepOrderIsSuccess() throws Exception {
        when(updateRepOrderValidator.validate(any(UpdateRepOrder.class)))
                .thenReturn(Optional.empty());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                .content(TestModelDataBuilder.getUpdateRepOrderJson())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }
}
