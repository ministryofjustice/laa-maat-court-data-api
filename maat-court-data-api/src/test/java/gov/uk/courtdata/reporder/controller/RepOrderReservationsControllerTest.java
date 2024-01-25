package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.builder.TestModelDataBuilder;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.exception.RequestedObjectNotFoundException;
import gov.uk.courtdata.reporder.service.ReservationsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static gov.uk.courtdata.builder.TestModelDataBuilder.RESERVATION_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepOrderReservationsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RepOrderReservationsControllerTest {
    private static final String BASE_END_POINT_URL = "/api/internal/v1/rep-orders/reservations";
    private static final String END_POINT_URL = BASE_END_POINT_URL + "/{id}";
    private static final int NON_EXISTENT_ID = 1234;
    private static final ReservationsEntity RESERVATIONS_ENTITY = ReservationsEntity.builder()
            .recordId(RESERVATION_ID)
            .build();

    @MockBean
    private ReservationsService reservationsService;
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
}
