package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.dto.RepOrderMvoDTO;
import gov.uk.courtdata.dto.RepOrderMvoRegDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.UpdateRepOrder;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.service.RepOrderMvoRegService;
import gov.uk.courtdata.reporder.service.RepOrderMvoService;
import gov.uk.courtdata.reporder.service.RepOrderService;
import gov.uk.courtdata.reporder.validator.UpdateAppDateCompletedValidator;
import gov.uk.courtdata.reporder.validator.UpdateRepOrderValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
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

    @MockBean
    private RepOrderMvoRegService repOrderMvoRegService;

    @MockBean
    private RepOrderMvoService repOrderMvoService;

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders";
    private static final String MVO_REG_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo-reg";

    private static final String MVO_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo";

    private static final String VEHICLE_OWNER = "vehicle-owner";
    private static final String VEHICLE_OWNER_INDICATOR_YES = "Y";

    private static final String CURRENT_REGISTRATION = "current-registration";

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
    public void givenValidMvoId_whenGetCurrentRegistrationFromRepOrderMvoRegIsInvoked_thenDataIsRetrieved() throws Exception {
        List<RepOrderMvoRegDTO> expected = List.of(TestModelDataBuilder.getRepOrderMvoRegDTO());
        when(repOrderMvoRegService.findByCurrentMvoRegistration(anyInt()))
                .thenReturn(expected);
        mvc.perform(MockMvcRequestBuilders.get(MVO_REG_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "/" + CURRENT_REGISTRATION))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].registration").value(expected.get(0).getRegistration()));
    }

    @Test
    public void givenInvalidMvoId_whenCurrentRegistrationFromRepOrderMvoRegIsInvoked_thenErrorIsThrown() throws Exception {
        when(repOrderMvoRegService.findByCurrentMvoRegistration(anyInt()))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order MVO Reg found for ID: 1234"));
        mvc.perform(MockMvcRequestBuilders.get(MVO_REG_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "/" + CURRENT_REGISTRATION))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("No Rep Order MVO Reg found for ID: 1234"));
    }

    @Test
    public void givenValidRepId_whenGetRepOrderMvoByRepIdAndVehicleOwnerIsInvoked_thenDataIsRetrieved() throws Exception {
        RepOrderMvoDTO expected = TestModelDataBuilder.getRepOrderMvoDTO();
        when(repOrderMvoService.findRepOrderMvoByRepIdAndVehicleOwner(anyInt(), anyString()))
                .thenReturn(expected);
        mvc.perform(MockMvcRequestBuilders.get(MVO_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vehicleOwner").value(expected.getVehicleOwner()));
    }

    @Test
    public void givenInvalidRepId_whenGetRepOrderMvoByRepIdAndVehicleOwnerIsInvoked_thenErrorIsThrown() throws Exception {
        when(repOrderMvoService.findRepOrderMvoByRepIdAndVehicleOwner(anyInt(), anyString()))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order MVO found for ID: 1234"));
        mvc.perform(MockMvcRequestBuilders.get(MVO_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("No Rep Order MVO found for ID: 1234"));
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
