package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.dto.RepOrderDTO;
import gov.uk.courtdata.dto.RepOrderMvoDTO;
import gov.uk.courtdata.dto.RepOrderMvoRegDTO;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.exception.ValidationException;
import gov.uk.courtdata.model.assessment.UpdateAppDateCompleted;
import gov.uk.courtdata.reporder.service.RepOrderMvoRegService;
import gov.uk.courtdata.reporder.service.RepOrderMvoService;
import gov.uk.courtdata.reporder.service.RepOrderService;
import gov.uk.courtdata.reporder.testutils.TestDataBuilder;
import gov.uk.courtdata.reporder.validator.UpdateAppDateCompletedValidator;
import gov.uk.courtdata.validator.MaatIdValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RepOrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class RepOrderControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders";
    private static final String MVO_REG_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo-reg";
    private static final String MVO_ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/rep-order-mvo";
    private static final String VEHICLE_OWNER_INDICATOR_YES = "Y";
    private static final String CURRENT_REGISTRATION = "current-registration";

    @Autowired
    private MockMvc mvc;
    @MockBean
    private UpdateAppDateCompletedValidator updateAppDateCompletedValidator;
    @MockBean
    private MaatIdValidator maatIdValidator;
    @MockBean
    private RepOrderService repOrderService;
    @MockBean
    private RepOrderMvoRegService repOrderMvoRegService;
    @MockBean
    private RepOrderMvoService repOrderMvoService;

    @Test
    void givenGetRequestWithValidRepIdAndNoOptionalParameters_whenFindIsInvoked_thenAssessmentIsRetrieved() throws Exception {
        RepOrderDTO expected = TestModelDataBuilder.getRepOrderDTO();
        when(repOrderService.find(anyInt(), anyBoolean()))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expected.getId()));
    }

    @Test
    void givenGetRequestWithValidRepIdAndNoOptionalParameters_whenFindIsInvoked_thenContributionsAndIojAppealAndRepOrderCCOutcomeIsRetrieved() throws Exception {
        RepOrderDTO expected = TestModelDataBuilder.getRepOrderDTO();
        expected.setContributions(asList(TestModelDataBuilder.getContributionsDTO()));
        expected.setIojAppeal(asList(TestModelDataBuilder.getIOJAppealDTO()));
        expected.setRepOrderCCOutcome(asList(TestModelDataBuilder.getRepOrderCCOutcomeDTO(1)));
        when(repOrderService.find(anyInt(), anyBoolean()))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.contributions.length()").value(1))
                .andExpect(jsonPath("$.repOrderCCOutcome.length()").value(1))
                .andExpect(jsonPath("$.iojAppeal.length()").value(1));
    }

    @Test
    void givenGetRequestWithValidRepIdAndOptionalParameters_whenFindInvoked_thenAssessmentIsRetrieved() throws Exception {
        RepOrderDTO expected = TestModelDataBuilder.getRepOrderDTO();
        when(repOrderService.find(anyInt(), eq(true)))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "?has_sentence_order_date=true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(expected.getId()));
    }

    @Test
    void givenGetRequestWithInvalidRepIdAndNoOptionalParameters_whenFindIsInvoked_thenErrorIsThrown() throws Exception {
        when(repOrderService.find(anyInt(), anyBoolean()))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order found for ID: 1234"));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("No Rep Order found for ID: 1234"));
    }

    @Test
    void givenHeadRequestWithValidRepIdAndNoOptionalParameters_whenFindIsInvoked_thenContentLengthIsOne() throws Exception {
        when(repOrderService.exists(anyInt()))
                .thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.head(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "1"));
    }

    @Test
    void givenHeadRequestWithInvalidRepIdAndNoOptionalParameters_whenFindIsInvoked_thenContentLengthIsZero() throws Exception {
        when(repOrderService.exists(anyInt()))
                .thenReturn(false);

        mvc.perform(MockMvcRequestBuilders.head(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "0"));
    }

    @Test
    void givenCorrectParameters_whenUpdateAppCompletedIsInvoked_thenCompletedDateShouldUpdated() throws Exception {
        when(updateAppDateCompletedValidator.validate(any(UpdateAppDateCompleted.class)))
                .thenReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/update-date-completed")
                .content(TestModelDataBuilder.getUpdateAppDateCompletedJson())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void givenIncorrectParameters_whenUpdateAppCompletedIsInvoked_thenErrorIsThrown() throws Exception {
        when(updateAppDateCompletedValidator.validate(any(UpdateAppDateCompleted.class)))
                .thenThrow(new ValidationException());

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/update-date-completed").content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenValidMvoId_whenFindByCurrentRegistrationIsInvoked_thenDataIsRetrieved() throws Exception {
        List<RepOrderMvoRegDTO> expected = List.of(TestModelDataBuilder.getRepOrderMvoRegDTO());
        when(repOrderMvoRegService.findByCurrentMvoRegistration(anyInt()))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(MVO_REG_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "/" + CURRENT_REGISTRATION))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].registration").value(expected.get(0).getRegistration()));
    }

    @Test
    void givenInvalidMvoId_whenFindByCurrentRegistrationIsInvoked_thenErrorIsThrown() throws Exception {
        when(repOrderMvoRegService.findByCurrentMvoRegistration(anyInt()))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order MVO Reg found for ID: 1234"));

        mvc.perform(MockMvcRequestBuilders.get(MVO_REG_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "/" + CURRENT_REGISTRATION))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("No Rep Order MVO Reg found for ID: 1234"));
    }

    @Test
    void givenValidRepId_whenFindByRepIdAndVehicleOwnerIsInvoked_thenDataIsRetrieved() throws Exception {
        RepOrderMvoDTO expected = TestModelDataBuilder.getRepOrderMvoDTO();
        when(repOrderMvoService.findRepOrderMvoByRepIdAndVehicleOwner(anyInt(), anyString()))
                .thenReturn(expected);

        mvc.perform(MockMvcRequestBuilders.get(MVO_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vehicleOwner").value(expected.getVehicleOwner()));
    }

    @Test
    void givenInvalidRepId_whenFindByRepIdAndVehicleOwnerIsInvoked_thenErrorIsThrown() throws Exception {
        when(repOrderMvoService.findRepOrderMvoByRepIdAndVehicleOwner(anyInt(), anyString()))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order MVO found for ID: 1234"));

        mvc.perform(MockMvcRequestBuilders.get(MVO_ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID + "?owner=" + VEHICLE_OWNER_INDICATOR_YES))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("No Rep Order MVO found for ID: 1234"));
    }

    @Test
    void givenIncorrectParameters_whenUpdateIsInvoked_thenErrorIsThrown() throws Exception {
        when(maatIdValidator.validate(any()))
                .thenThrow(new ValidationException());

        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL).content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenCorrectParameters_whenUpdateIsInvoked_thenUpdateRepOrderIsSuccess() throws Exception {
        when(maatIdValidator.validate(any()))
                .thenReturn(Optional.empty());
        when(repOrderService.update(any())).thenReturn(TestModelDataBuilder.getRepOrderDTO());
        mvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .content(TestModelDataBuilder.getUpdateRepOrderJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TestModelDataBuilder.REP_ID));
    }

    @Test
    void givenCorrectParameters_whenCreateIsInvoked_thenOkResponseAndRepOrderIsReturned() throws Exception {
        when(repOrderService.create(any())).thenReturn(TestModelDataBuilder.getRepOrderDTO());

        mvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(TestModelDataBuilder.getCreateRepOrderJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(TestModelDataBuilder.REP_ID));
    }

    @Test
    void givenValidRepId_whenDeleteIsInvoked_thenReturnOkResponse() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(ENDPOINT_URL+"/12345678")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(repOrderService, times(1)).delete(12345678);
    }

    @Test
    void givenValidRepId_whenIOJAssessorDetailsGetRequestIsMade_thenIOJAssessorDetailsAreReturned() throws Exception {
        when(repOrderService.findIOJAssessorDetails(TestModelDataBuilder.REP_ID))
                .thenReturn(TestDataBuilder.getIOJAssessorDetails());

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + TestModelDataBuilder.REP_ID+"/ioj-assessor-details"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(TestDataBuilder.REP_ORDER_CREATOR_NAME))
                .andExpect(jsonPath("$.userName").value(TestDataBuilder.REP_ORDER_CREATOR_USER_NAME));
    }

    @Test
    void givenUnknownRepId_whenIOJAssessorDetailsGetRequestIsMade_thenNotFoundResponseIsReturned() throws Exception {
        int unknownRepId = 1245;
        when(repOrderService.findIOJAssessorDetails(unknownRepId))
                .thenThrow(new RequestedObjectNotFoundException("Unable to find IOJAssessorDetails for repId: [1245]"));

        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/" + unknownRepId +"/ioj-assessor-details"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Unable to find IOJAssessorDetails for repId: [1245]"));
    }
}
