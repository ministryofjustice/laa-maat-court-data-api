package gov.uk.courtdata.authorization.controller;

import gov.uk.courtdata.authorization.service.AuthorizationService;
import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.enums.LoggingData;
import gov.uk.courtdata.model.AuthorizationResponse;
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
@ConditionalOnProperty(value = "feature.authorizationEndpoints", havingValue = "true")
public class AuthorizationController {

    private final AuthorizationService authorizationService;

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

        log.info("Check-Role-Action Request Received");
        boolean isAuthorized = authorizationService.isRoleActionAuthorized(username, action);
        return ResponseEntity.ok(AuthorizationResponse.builder().result(isAuthorized).build());
    }
}
