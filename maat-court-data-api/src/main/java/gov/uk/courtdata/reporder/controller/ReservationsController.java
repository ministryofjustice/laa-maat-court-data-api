package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.reporder.service.ReservationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Rest API for Reservations")
@RequestMapping("/api/internal/v1/rep-orders/reservations")
public class ReservationsController {
    private final ReservationsService reservationsService;

    @GetMapping(value = "/{id}")
    @Operation(description = "Retrieve a reservation")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public ReservationsEntity getReservation(@PathVariable Integer id) {
        return reservationsService.retrieve(id);
    }

    @PostMapping
    @Operation(description = "Create a Reservation")
    @StandardApiResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<Void> createReservation(@RequestBody ReservationsEntity reservationsEntity) {
        reservationsService.create(reservationsEntity);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{id}")
    @Operation(description = "Update a Reservation")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public ResponseEntity<Void> updateReservation(@PathVariable Integer id, @RequestBody ReservationsEntity reservationsEntity) {
        reservationsService.update(id, reservationsEntity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(description = "Delete a Reservation")
    @StandardApiResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        reservationsService.delete(id);
        return ResponseEntity.ok().build();
    }
}
