package gov.uk.courtdata.users.controller;

import gov.uk.courtdata.dto.ErrorDTO;
import gov.uk.courtdata.dto.UserSummaryDTO;
import gov.uk.courtdata.model.authorization.AuthorizationResponse;
import gov.uk.courtdata.users.service.UserSummaryService;
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
@Tag(name = "User Summary", description = "Rest API to manage user information")
@RequestMapping("${api-endpoints.user-domain}")
public class UserSummaryController {

    private final UserSummaryService userSummaryService;


    @GetMapping(value = "/summary/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Check role action privileges")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthorizationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    @ApiResponse(responseCode = "500", description = "Server Error.", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorDTO.class)))
    public ResponseEntity<UserSummaryDTO> getUserSummary(
            @PathVariable String username) {
        log.info("Get User Summary - request received");
        return ResponseEntity.ok(userSummaryService.getUserSummary(username));
    }

}
