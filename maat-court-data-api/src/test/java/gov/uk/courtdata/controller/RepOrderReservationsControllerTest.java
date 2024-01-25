package gov.uk.courtdata.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationsController.class)
@AutoConfigureMockMvc(addFilters = false)
class RepOrderReservationsControllerTest {

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

    @Test
    void givenAValidParameters_whenGetReservationByRecordNameAndRecordIdIsInvoked_thenReturnReservationsEntity() throws Exception {
        when(reservationsRepositoryHelper.getReservationByRecordNameAndRecordId(CourtDataConstants.RESERVATION_RECORD_NAME, TestModelDataBuilder.REP_ID))
                .thenReturn(Optional.ofNullable(ReservationsEntity.builder()
                        .recordName(TestModelDataBuilder.RESERVATION_RECORD_NAME)
                        .recordId(TestModelDataBuilder.REP_ID)
                        .userSession(TestModelDataBuilder.USER_SESSION)
                        .userName(TestModelDataBuilder.USER_NAME)
                        .reservationDate(LocalDateTime.now())
                        .expiryDate(LocalDateTime.now())
                        .build()
                ));
        mvc.perform(MockMvcRequestBuilders.get( ENDPOINT_URL + "/recordname/" + CourtDataConstants.RESERVATION_RECORD_NAME + "/recordid/" + TestModelDataBuilder.REP_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.recordId").value(TestModelDataBuilder.REP_ID));
    }

    @Test
    void givenIncorrectParameters_whenGetReservationByRecordNameAndRecordIdIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/recordname/" + CourtDataConstants.RESERVATION_RECORD_NAME + "/recordid/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenAValidParameters_whenGetReservationByUserNameIsInvoked_thenReturnReservationsEntity() throws Exception {
        when(reservationsRepositoryHelper.getReservationByUserName(TestModelDataBuilder.USER_NAME))
                .thenReturn(Optional.ofNullable(ReservationsEntity.builder()
                        .recordName(TestModelDataBuilder.RESERVATION_RECORD_NAME)
                        .recordId(TestModelDataBuilder.REP_ID)
                        .userSession(TestModelDataBuilder.USER_SESSION)
                        .userName(TestModelDataBuilder.USER_NAME)
                        .reservationDate(LocalDateTime.now())
                        .expiryDate(LocalDateTime.now())
                        .build()
                ));
        mvc.perform(MockMvcRequestBuilders.get( ENDPOINT_URL + "/username/" + TestModelDataBuilder.USER_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.recordId").value(TestModelDataBuilder.REP_ID));
    }

    @Test
    void givenIncorrectParameters_whenGetReservationByUserNameIsInvoked_thenErrorIsThrown() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/username/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }



}