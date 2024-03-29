package gov.uk.courtdata.authorization.controller;

import gov.uk.courtdata.authorization.service.AuthorizationService;
import gov.uk.courtdata.authorization.validator.UserReservationValidator;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.model.authorization.UserReservation;
import gov.uk.courtdata.model.authorization.UserSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Authorization", description = "Rest API for performing role authorization checks")
@RequestMapping("${api-endpoints.assessments-domain}/authorization")
public class AuthorizationController {

    private final AuthorizationService authorizationService;
    private final UserReservationValidator userReservationValidator;

    @GetMapping(value = "/users/{username}/actions/{action}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check role action privileges")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<AuthorizationResponse> isRoleActionAuthorized(
            @PathVariable String username, @PathVariable String action) {

        log.info("Check role action - request received");
        boolean isAuthorized = authorizationService.isRoleActionAuthorized(username, action);
        return ResponseEntity.ok(AuthorizationResponse.builder().result(isAuthorized).build());
    }

    @GetMapping(value = "/users/{username}/work-reasons/{nworCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check new work order reason")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<AuthorizationResponse> isNewWorkReasonAuthorized(
            @PathVariable String username, @PathVariable String nworCode) {

        log.info("Check new work order reason - request received");
        boolean isAuthorized = authorizationService.isNewWorkReasonAuthorized(username, nworCode);
        return ResponseEntity.ok(AuthorizationResponse.builder().result(isAuthorized).build());
    }

    @GetMapping(value = "/users/{username}/reservations/{reservationId}/sessions/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check the reservation status of a record")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<Object> isReserved(
            @PathVariable String username, @PathVariable Integer reservationId, @PathVariable String sessionId) {

        log.info("Check reservation status - request received");

        UserReservation userReservation = UserReservation.builder()
                .reservationId(reservationId)
                .session(UserSession.builder()
                        .id(sessionId)
                        .username(username)
                        .build()
                ).build();

        userReservationValidator.validate(userReservation);
        boolean isReserved = authorizationService.isReserved(userReservation);
        return ResponseEntity.ok(AuthorizationResponse.builder().result(isReserved).build());
    }
}
