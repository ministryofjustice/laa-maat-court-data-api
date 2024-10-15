package gov.uk.courtdata.correspondence.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.gov.justice.laa.crime.enums.contribution.CorrespondenceStatus;

import static gov.uk.courtdata.builder.TestModelDataBuilder.CORRESPONDENCE_STATUS;
import static gov.uk.courtdata.builder.TestModelDataBuilder.REP_ID;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CorrespondenceStateController.class)
@AutoConfigureMockMvc(addFilters = false)
class CorrespondenceStateControllerTest {

    private static final int INVALID_REP_ID = 1235;
    private final ObjectMapper mapper = new ObjectMapper();
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/rep-orders/{repId}/correspondence-state";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CorrespondenceStateService correspondenceStateService;

    @Test
    void givenCorrectRepId_whenFindIsReturned() throws Exception {
        when(correspondenceStateService.getCorrespondenceStatus(REP_ID))
                .thenReturn(CorrespondenceStatus.APPEAL_CC);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL, REP_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$")
                                   .value(CorrespondenceStatus.APPEAL_CC.getStatus()));
    }

    @Test
    void givenInvalidRepId_whenFindIsInvoked_thenNotFoundIsThrown() throws Exception {
        when(correspondenceStateService.getCorrespondenceStatus(INVALID_REP_ID))
                .thenThrow(new RequestedObjectNotFoundException("No Rep Order found for ID: " + INVALID_REP_ID));

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL, INVALID_REP_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenBlankRepId_whenFindIsInvoked_thenBadRequestIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL, "BLANK"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectParameters_whenCreateIsInvoked_thenCorrespondenceStateIsCreated() throws Exception {
        CorrespondenceStatus status = CorrespondenceStatus.APPEAL_CC;

        when(correspondenceStateService.createCorrespondenceState(REP_ID, status))
                .thenReturn(status);

        String jsonBody = mapper.writeValueAsString(status);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL, REP_ID)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBody));
    }

    @Test
    void givenBlankRepId_whenCreateIsInvoked_thenBadRequestIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL, "BLANK")
                                .content(mapper.writeValueAsString(
                                        CorrespondenceStatus.getFrom(CORRESPONDENCE_STATUS)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenEmptyStatus_whenCreateIsInvoked_thenBadRequestIsThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL, REP_ID)
                                .content(mapper.writeValueAsString(""))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenCorrectParameters_whenUpdateIsInvoked_thenCorrespondenceStateIsUpdatedOrCreated() throws Exception {
        CorrespondenceStatus status = CorrespondenceStatus.getFrom(CORRESPONDENCE_STATUS);
        when(correspondenceStateService.updateCorrespondenceState(REP_ID, status))
                .thenReturn(status);
        String jsonBody = mapper.writeValueAsString(status);

        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT_URL, REP_ID)
                                .content(jsonBody)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonBody));
    }

}
