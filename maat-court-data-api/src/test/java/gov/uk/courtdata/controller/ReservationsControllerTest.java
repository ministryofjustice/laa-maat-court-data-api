package gov.uk.courtdata.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReservationsController.class)
class ReservationsControllerTest {

    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/reservations/";
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ReservationsRepositoryHelper reservationsRepositoryHelper;

    @Test
    void givenIncorrectParameters_whenIsMaatRecordLockedIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenIsMaatRecordLockedIsInvoked_thenReturnTrue() throws Exception {
        when(reservationsRepositoryHelper.isMaatRecordLocked(TestModelDataBuilder.REP_ID))
                .thenReturn(Boolean.TRUE);
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(Boolean.TRUE));
    }
}