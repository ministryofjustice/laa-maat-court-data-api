package gov.uk.courtdata.reporder.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.annotation.StandardApiResponse;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.reporder.service.ReservationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.justice.laa.crime.commons.common.Constants;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Rest API for Reservations")
@RequestMapping("/api/internal/v1")
public class ReservationsController {
    private final ReservationsService reservationsService;

    private final ReservationsRepositoryHelper reservationsRepositoryHelper;

    @GetMapping(value = "/assessment/reservations/{maatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check if a given maatId is Locked through Reservations table")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @NotFoundApiResponse
    @StandardApiResponse
    public ResponseEntity<Boolean> isMaatRecordLocked(
            @PathVariable int maatId,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = CourtDataConstants.LAA_TRANSACTION_ID, required = false) String laaTransactionId) {
        LoggingData.MAAT_ID.putInMDC(maatId);
        log.info(String.format("Check if maatId is locked - %d {}", maatId));
        return ResponseEntity.ok(reservationsRepositoryHelper.isMaatRecordLocked(maatId));
    }

    @GetMapping(value = "/assessment/reservations/recordname/{recordName}/recordid/{recordId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get the reservation status object of a record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @StandardApiResponse
    public ResponseEntity<ReservationsEntity> getReservationByRecordNameAndRecordId(
            @PathVariable String recordName, @PathVariable Integer recordId,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = Constants.LAA_TRANSACTION_ID, required = false) String laaTransactionId) {
        log.info("Check reservation status - request received");
        Optional<ReservationsEntity> reservationsEntity = reservationsRepositoryHelper.getReservationByRecordNameAndRecordId(recordName,recordId);
        return reservationsEntity.map(c -> ResponseEntity.ok().body(c))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/assessment/reservations/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get the reservation status object of a record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @StandardApiResponse
    public ResponseEntity<ReservationsEntity> getReservationByUsername(
            @PathVariable String username,
            @RequestHeader(value = Constants.LAA_TRANSACTION_ID, required = false) String laaTransactionId) {
        log.info("Check reservation status - request received");
        Optional<ReservationsEntity> reservationsEntity = reservationsRepositoryHelper.getReservationByUserName(username);
        return reservationsEntity.map(c -> ResponseEntity.ok().body(c))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/rep-orders/reservations/{id}")
    @Operation(description = "Retrieve a reservation")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public ReservationsEntity getReservation(@PathVariable Integer id) {
        return reservationsService.retrieve(id);
    }

    @PostMapping(value = "/rep-orders/reservations")
    @Operation(description = "Create a Reservation")
    @StandardApiResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<Void> createReservation(@RequestBody ReservationsEntity reservationsEntity) {
        reservationsService.create(reservationsEntity);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/rep-orders/reservations/{id}")
    @Operation(description = "Update a Reservation")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @StandardApiResponse
    @NotFoundApiResponse
    public ResponseEntity<Void> updateReservation(@PathVariable Integer id, @RequestBody ReservationsEntity reservationsEntity) {
        reservationsService.update(id, reservationsEntity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/rep-orders/reservations/{id}")
    @Operation(description = "Delete a Reservation")
    @StandardApiResponse
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        reservationsService.delete(id);
        return ResponseEntity.ok().build();
    }
}
