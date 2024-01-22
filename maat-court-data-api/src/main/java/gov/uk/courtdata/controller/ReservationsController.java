package gov.uk.courtdata.controller;

import gov.uk.courtdata.annotation.NotFoundApiResponse;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.entity.ReservationsEntity;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static gov.uk.courtdata.enums.LoggingData.LAA_TRANSACTION_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "reservations", description = "Rest API for Reservations")
@RequestMapping("${api-endpoints.assessments-domain}/reservations")
public class ReservationsController {

    private final ReservationsRepositoryHelper reservationsRepositoryHelper;

    @GetMapping(value = "/{maatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check if a given maatId is Locked through Reservations table")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @NotFoundApiResponse
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Boolean> isMaatRecordLocked(
            @PathVariable int maatId,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = CourtDataConstants.LAA_TRANSACTION_ID, required = false) String laaTransactionId) {
        MDC.put(LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info(String.format("Check if maatId is locked - %d {}", maatId));
        return ResponseEntity.ok(reservationsRepositoryHelper.isMaatRecordLocked(maatId));
    }


    @GetMapping(value = "/recordname/{recordName}/recordid/{recordId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get the reservation status object of a record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<ReservationsEntity> getReservationByRecordNameAndRecordId(
            @PathVariable String recordName, @PathVariable Integer recordId,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Check reservation status - request received");
        Optional<ReservationsEntity> reservationsEntity = reservationsRepositoryHelper.getReservationByRecordNameAndRecordId(recordName,recordId);
        return reservationsEntity.map(c -> ResponseEntity.ok().body(c))
                .orElse(ResponseEntity.notFound().build());
    }



    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get the reservation status object of a record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<ReservationsEntity> getReservationByUsername(
            @PathVariable String username,
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {
        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Check reservation status - request received");
        Optional<ReservationsEntity> reservationsEntity = reservationsRepositoryHelper.getReservationByUserName(username);
        return reservationsEntity.map(c -> ResponseEntity.ok().body(c))
                .orElse(ResponseEntity.notFound().build());
    }

}
