package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.reporder.service.ReservationsService;
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

import static gov.uk.courtdata.builder.TestModelDataBuilder.RESERVATION_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ReservationsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationsControllerTest {
    private static final String ENDPOINT_URL = "/api/internal/v1/assessment/reservations/";
    private static final String BASE_END_POINT_URL = "/api/internal/v1/rep-orders/reservations";
    private static final String END_POINT_URL = BASE_END_POINT_URL + "/{id}";
    private static final int NON_EXISTENT_ID = 1234;
    private static final ReservationsEntity RESERVATIONS_ENTITY = ReservationsEntity.builder()
            .recordId(RESERVATION_ID)
            .build();

    @MockBean
    private ReservationsRepositoryHelper reservationsRepositoryHelper;

    @MockBean
    ReservationsService reservationsService;
    @Autowired
    private MockMvc mvc;

    @Test
    void givenValidId_whenGetReservationsIsCalled_thenReservationsEntityIsReturned() throws Exception {
        when(reservationsService.retrieve(RESERVATION_ID))
                .thenReturn(RESERVATIONS_ENTITY);

        mvc.perform(MockMvcRequestBuilders.get(END_POINT_URL, RESERVATION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"recordId\":" + RESERVATION_ID + "}"));
    }

    @Test
    void givenNonExistentID_whenGetReservationsIsCalled_thenErrorResponseIsReturned() throws Exception {
        when(reservationsService.retrieve(NON_EXISTENT_ID))
                .thenThrow(new RequestedObjectNotFoundException("No Reservations found with Id: " + NON_EXISTENT_ID));

        mvc.perform(MockMvcRequestBuilders.get(END_POINT_URL, NON_EXISTENT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"code\":\"NOT_FOUND\",\"message\":\"No Reservations found with Id: " + NON_EXISTENT_ID + "\"}"));
    }

    @Test
    void givenValidData_whenCreateReservationIsCalled_thenReservationIsCreated() throws Exception {

        ReservationsEntity reservationsEntity = TestModelDataBuilder.getReservationsEntity();

        mvc.perform(MockMvcRequestBuilders.post(BASE_END_POINT_URL)
                        .content(reservationEntityJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(reservationsService, times(1)).create(reservationsEntity);
    }

    @Test
    void givenValidData_whenUpdateRepOrderEquityCalled_thenSuccessfullyUpdateRepOrderEquity() throws Exception {

        ReservationsEntity reservationsEntity = TestModelDataBuilder.getReservationsEntity();

        mvc.perform(MockMvcRequestBuilders.put(END_POINT_URL, RESERVATION_ID)
                        .content(reservationEntityJson())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(reservationsService, times(1)).update(RESERVATION_ID, reservationsEntity);
    }

    @Test
    void givenValidId_whenDeleteReservationIsCalled_thenReservationIsSuccessfullyDeleted() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(END_POINT_URL, RESERVATION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(reservationsService, times(1)).delete(RESERVATION_ID);
    }

    private static String reservationEntityJson() {
        return """
                {
                  "recordId": %d,
                  "reservationDate": null,
                  "expiryDate": null,
                  "recordName": "mock-record",
                  "userName": "mock-user",
                  "userSession": "mock-session"
                }
                """.formatted(RESERVATION_ID);
    }

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
