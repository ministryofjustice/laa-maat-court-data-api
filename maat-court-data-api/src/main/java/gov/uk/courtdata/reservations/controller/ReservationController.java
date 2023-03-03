package gov.uk.courtdata.reservations.controller;

import com.amazonaws.xray.spring.aop.XRayEnabled;
import gov.uk.courtdata.authentication.service.AuthenticationService;
import gov.uk.courtdata.constants.CourtDataConstants;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.OutstandingAssessmentResultDTO;
import gov.uk.courtdata.dto.ReservationDTO;
import gov.uk.courtdata.prosecutionconcluded.helper.ReservationsRepositoryHelper;
import gov.uk.courtdata.reservations.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

import static gov.uk.courtdata.enums.LoggingData.LAA_TRANSACTION_ID;

@RestController
@RequestMapping("/api/internal/v1/reservation")
@Slf4j
@RequiredArgsConstructor
@XRayEnabled
@Tag(name = "Reservation", description = "Rest API managing application reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationsRepositoryHelper reservationsRepositoryHelper;

    @GetMapping(value = "/{maatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get the reservation for a given MAAT ID")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ReservationDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)))
   public ResponseEntity<ReservationDTO> getReservation(@PathVariable int maatId) {
        return ResponseEntity.ok().body(reservationService.getReservation(maatId));
    }


    @GetMapping(value = "assessment/{maatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check if a given maatId is Locked through Reservations table")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @ApiResponse(responseCode = "404", description = "Not Found.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
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
}
