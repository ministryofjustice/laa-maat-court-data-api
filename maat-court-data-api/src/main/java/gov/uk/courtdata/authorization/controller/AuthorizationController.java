package gov.uk.courtdata.authorization.controller;

import gov.uk.courtdata.authorization.service.AuthorizationService;
import gov.uk.courtdata.authorization.validator.UserReservationValidator;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.model.authorization.UserReservation;
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

@RestController
@RequestMapping("/authorization")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Authorization", description = "Rest API for performing role authorization checks")
public class AuthorizationController {

    private final AuthorizationService authorizationService;
    private final UserReservationValidator userReservationValidator;

    @GetMapping(value = "/users/{username}/validation/action/{action}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check role action privileges")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<AuthorizationResponse> isRoleActionAuthorized(
            @PathVariable String username, @PathVariable String action,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);

        log.info("Check role action - request received");
        boolean isAuthorized = authorizationService.isRoleActionAuthorized(username, action);
        return ResponseEntity.ok(AuthorizationResponse.builder().result(isAuthorized).build());
    }

    @GetMapping(value = "/users/{username}/validation/nwor/{nworCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check new work order reason")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<AuthorizationResponse> isNewWorkReasonAuthorized(
            @PathVariable String username, @PathVariable String nworCode,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);

        log.info("Check new work order reason - request received");
        boolean isAuthorized = authorizationService.isNewWorkReasonAuthorized(username, nworCode);
        return ResponseEntity.ok(AuthorizationResponse.builder().result(isAuthorized).build());
    }

    @PostMapping(value = "/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check the reservation status of a record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> isReserved(
            @Parameter(description = "Reservation data", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserReservation.class))) @RequestBody UserReservation userReservation,
            @Parameter(description = "Used for tracing calls")
            @RequestHeader(value = "Laa-Transaction-Id", required = false) String laaTransactionId) {

        MDC.put(LoggingData.LAA_TRANSACTION_ID.getValue(), laaTransactionId);
        log.info("Check reservation status - request received");
        userReservationValidator.validate(userReservation);
        boolean isReserved = authorizationService.isReserved(userReservation);
        return ResponseEntity.ok(AuthorizationResponse.builder().result(isReserved).build());
    }
}
