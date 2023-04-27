package gov.uk.courtdata.correspondence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.correspondence.dto.CorrespondenceStateDTO;
import gov.uk.courtdata.correspondence.service.CorrespondenceStateService;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CorrespondenceStateController.class)
public class CorrespondenceStateControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/correspondence-state";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CorrespondenceStateService correspondenceStateService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void givenCorrectRepId_whenGetIsInvoked_thenCorrectStatusIsReturned() throws Exception {
        when(correspondenceStateService.getCorrespondenceStatus(REP_ID)).thenReturn("appealCC");
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/" + REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().string("appealCC"));
        verify(correspondenceStateService).getCorrespondenceStatus(REP_ID);
    }

    @Test
    void givenInvalidRepId_whenGetIsInvoked_thenNotFoundIsThrown() throws Exception {
        when(correspondenceStateService.getCorrespondenceStatus(0))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order found for ID: 0"));
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/0"))
                .andExpect(status().isNotFound());
        verify(correspondenceStateService).getCorrespondenceStatus(0);
    }

    @Test
    void givenNullRepId_whenGetIsInvoked_thenBadRequestIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/repId/null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectParameters_whenCreateIsInvoked_thenCorrespondenceStateIsCreated() throws Exception {
        CorrespondenceStateDTO correspondenceStateDTO = buildCorrespondenceStateDTO(REP_ID, "none");
        when(correspondenceStateService.createCorrespondenceState(correspondenceStateDTO))
                .thenReturn(correspondenceStateDTO);
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(mapper.writeValueAsString(correspondenceStateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(correspondenceStateDTO)));
        verify(correspondenceStateService).createCorrespondenceState(correspondenceStateDTO);
    }

    @Test
    void givenNullRepId_whenCreateIsInvoked_thenBadRequestIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL)
                        .content(mapper.writeValueAsString(buildCorrespondenceStateDTO(null, "appealCC")))
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
    void givenCorrectParameters_whenUpdateIsInvoked_thenCorrespondenceStateIsUpdated() throws Exception {
        CorrespondenceStateDTO correspondenceStateDTO = buildCorrespondenceStateDTO(REP_ID, "none");
        when(correspondenceStateService.updateCorrespondenceState(correspondenceStateDTO))
                .thenReturn(correspondenceStateDTO);
        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .content(mapper.writeValueAsString(correspondenceStateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(correspondenceStateDTO)));
        verify(correspondenceStateService).updateCorrespondenceState(correspondenceStateDTO);
    }

    @Test
    void givenNullRepId_whenUpdateIsInvoked_thenBadRequestIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .content(mapper.writeValueAsString(buildCorrespondenceStateDTO(null, "appealCC")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNonExistentRepId_whenUpdateIsInvoked_thenNotFoundIsThrown() throws Exception {
        CorrespondenceStateDTO correspondenceStateDTO = buildCorrespondenceStateDTO(REP_ID, "appealCC");
        when(correspondenceStateService.updateCorrespondenceState(correspondenceStateDTO))
                .thenThrow(new RequestedObjectNotFoundException("No corresponsdence state found for repId: 1234"));

        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL)
                        .content(mapper.writeValueAsString(correspondenceStateDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(correspondenceStateService).updateCorrespondenceState(correspondenceStateDTO);
    }

    private CorrespondenceStateDTO buildCorrespondenceStateDTO(Integer repId, String status) {
        return CorrespondenceStateDTO.builder()
                .repId(repId)
                .status(status)
                .build();
    }

}
