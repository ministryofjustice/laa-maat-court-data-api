package gov.uk.courtdata.correspondence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.correspondence.dto.CorrespondenceStateDTO;
import gov.uk.courtdata.correspondence.service.CorrespondenceStateService;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static gov.uk.courtdata.builder.TestModelDataBuilder.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CorrespondenceStateController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CorrespondenceStateControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/correspondence-state";
    private static final int INVALID_REP_ID = 1235;
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CorrespondenceStateService correspondenceStateService;

    @Test
    void givenCorrectRepId_whenGetStatusIsInvoked_thenCorrectStatusIsReturned() throws Exception {
        when(correspondenceStateService.getCorrespondenceStatus(REP_ID)).thenReturn(CORRESPONDENCE_STATUS);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().string(CORRESPONDENCE_STATUS));
        verify(correspondenceStateService).getCorrespondenceStatus(REP_ID);
    }

    @Test
    void givenInvalidRepId_whenGetStatusIsInvoked_thenNotFoundIsThrown() throws Exception {
        when(correspondenceStateService.getCorrespondenceStatus(INVALID_REP_ID))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order found for ID: " + INVALID_REP_ID));

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/"+INVALID_REP_ID))
                .andExpect(status().isNotFound());
        verify(correspondenceStateService).getCorrespondenceStatus(INVALID_REP_ID);
    }

    @Test
    void givenNullRepId_whenGetStatucIsInvoked_thenBadRequestIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectParameters_whenCreateIsInvoked_thenCorrespondenceStateIsCreated() throws Exception {
        CorrespondenceStateDTO correspondenceStateDTO = buildCorrespondenceStateDTO(REP_ID, CORRESPONDENCE_STATUS);
        when(correspondenceStateService.createCorrespondenceState(correspondenceStateDTO)).thenReturn(correspondenceStateDTO);
        String jsonBody = mapper.writeValueAsString(correspondenceStateDTO);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBody));
        verify(correspondenceStateService).createCorrespondenceState(correspondenceStateDTO);
    }

    @Test
    void givenNullRepId_whenCreateIsInvoked_thenBadRequestIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(mapper.writeValueAsString(buildCorrespondenceStateDTO(null, CORRESPONDENCE_STATUS)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenEmptyStatus_whenCreateIsInvoked_thenBadRequestIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(mapper.writeValueAsString(buildCorrespondenceStateDTO(REP_ID, "")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectParameters_whenUpdateIsInvoked_thenCorrespondenceStateIsUpdatedOrCreated() throws Exception {
        CorrespondenceStateDTO correspondenceStateDTO = buildCorrespondenceStateDTO(REP_ID, CORRESPONDENCE_STATUS);
        when(correspondenceStateService.updateCorrespondenceState(correspondenceStateDTO))
                .thenReturn(correspondenceStateDTO);
        String jsonBody = mapper.writeValueAsString(correspondenceStateDTO);

        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBody));
        verify(correspondenceStateService).updateCorrespondenceState(correspondenceStateDTO);
    }

}
